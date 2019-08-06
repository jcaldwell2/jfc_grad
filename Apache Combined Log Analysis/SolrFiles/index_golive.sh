hadoop --config /etc/hadoop/conf \
jar /opt/cloudera/parcels/CDH/lib/hbase-solr/tools/hbase-indexer-mr-*-job.jar \
--conf /etc/hbase/conf/hbase-site.xml -D 'mapred.child.java.opts=-Xmx300m' \
--hbase-indexer-file $HOME/hbase-collection-ruweb/conf/morphline-hbase-mapper.xml \
--zk-host node00.sun:2181,node01.sun2181/solr \
--collection hbase-collection-ruweb \
--go-live \
--log4j $HOME/hbase-collection-ruweb/conf/log4j.properties