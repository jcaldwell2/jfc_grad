hadoop --config /etc/hadoop/conf \
jar /opt/cloudera/parcels/CDH/lib/hbase-solr/tools/hbase-indexer-mr-*-job.jar \
--conf /etc/hbase/conf/hbase-site.xml -D 'mapred.child.java.opts=-Xmx300m' \
--hbase-indexer-file $HOME/ITEC695/index_migos/conf/morphline-hbase-mapper.xml \
--zk-host node00.sun:2181,node01.sun2181/solr \
--collection index_migos \
--go-live \
--log4j $HOME/ITEC695/index_migos/conf/log4j.properties