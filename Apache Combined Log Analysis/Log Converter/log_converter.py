##
##  Apache Combined Log Converter
##  by James Caldwell
##  October 5, 2018
##


from argparse import ArgumentParser

import os.path
import datetime as dt

arg_error = "Error executing program, use -h for help"

fields = ["id,", "ip,", "identd,", "userid,", "timestamp,", "client_request_line,", "status_code,", "bytes,",
          "referer,", "user_agent,", "mu\n"]
num_fields = len(fields) - 1

##print("Field length is " + str(num_fields))
headers = ''.join(fields)

start = dt.datetime.now()

parser = ArgumentParser()
parser.add_argument("-f", "--file", dest="input_file", help="Open specified file")
args = parser.parse_args()
input_file = args.input_file

outfile = "out.csv"
count = 1


def format_line(line):
    line = line.replace("[", "\"",1)
    line = line.replace("]", "\"",1)
    quotes = 0
    new_fields = []
    temp_string = ""
    global count
    global num_fields
    element_count = 0

    new_fields.append(str(count) + ",")

    for word in line.split():


        word = word.replace(","," ")

        if word.count("\"") == 0 and quotes == 0:
           if "mu" in word:
               new_fields.append(word)
               element_count += 1
           else:
                new_fields.append(word + ",")
                element_count += 1
        if word.count("\"") == 0 and quotes == 1:
            temp_string += " " + word
        if word.count("\"\\\"") == 1 and quotes == 0:
            temp_string += " " + word
            quotes += 1
        if word.count("\"\"") == 1 and quotes == 1:
            quotes += 1
            temp_string += " " + word
            new_fields.append(temp_string + ",")
            element_count += 1
            temp_string = ""
        if word.count("\"") == 1 and quotes == 1:
            quotes += 1
            temp_string += " " + word
            new_fields.append(temp_string + ",")
            element_count += 1
            temp_string = ""
        if word.count("\"") == 1 and quotes == 0:
            temp_string += word

            quotes += 1
        if word.count("\"") == 2 and quotes == 0:
            new_fields.append(word + ",")
            element_count += 1

        if quotes == 2:
            quotes = 0
    ## Just going to assume for now if the fields do not matchup with the header count then it is
    ## in Common Log format and data is missing from the last two fields so lets just add the "-" that donotes no
    ## information provided so we do not have null values
    if element_count != num_fields:
        new_fields.append("\"-\",")
        new_fields.append("\"-\",")
        new_fields.append("\"-\"")
  ##  print("element count is " + str(element_count))

    final_string = ''.join(new_fields)
    count += 1

    return final_string + "\n"


def write_line(line):
    with open(outfile, "a") as out:
        out.write(line)


def read_line(file):
    with file as fp:
        line = fp.readline()
        cnt = 1

        while line:
            line = format_line(line)

            write_line(line)
            line = fp.readline()
            cnt += 1
            print("Converting line........" + str(count))

def main():
    try:

        global input_file

        with open(input_file)as input_file:
            text = input_file
            if os.path.exists(outfile):
                os.remove(outfile)
                with open(outfile, "a") as out:
                    out.write(headers)
                    print("The file " + outfile + " replaced the previous file")
            else:
                try:
                    with open(outfile, "a") as out:
                        out.write(headers)
                        print("The file " + outfile + " has been created")
                except IOError:
                    print("File write error")
            read_line(text)
            end = dt.datetime.now()
            print("<---------- Conversion took {} s. ---------->".format((end - start).seconds))


    except TypeError:
        print(arg_error)

main()
