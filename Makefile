build: src/*.java | ./build/
	./hadoop-3.4.1/bin/hadoop com.sun.tools.javac.Main src/*.java -d build
	jar cf wc.jar -C build .

./build/:
	mkdir -p build/

clean:
	rm -rf build/
	rm wc.jar

run:
	./hadoop-3.4.1/bin/hadoop jar wc.jar Main

start:
	./hadoop-3.4.1/sbin/start-all.sh

stop:
	./hadoop-3.4.1/sbin/stop-all.sh

format_and_start:
	./hadoop-3.4.1/sbin/stop-all.sh
	./hadoop-3.4.1/bin/hadoop namenode -format
	./hadoop-3.4.1/sbin/start-all.sh
	./hadoop-3.4.1/bin/hdfs dfs -mkdir /user
	./hadoop-3.4.1/bin/hdfs dfs -mkdir /user/csf
	./hadoop-3.4.1/bin/hdfs dfs -mkdir /user/csf/input
	./hadoop-3.4.1/bin/hdfs dfs -put ./data/*.csv /user/csf/input

show:
	./hadoop-3.4.1/bin/hdfs dfs -cat output/part-r-*
