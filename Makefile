build: src/*.java | ./build/
	./hadoop-3.4.1/bin/hadoop com.sun.tools.javac.Main src/*.java -d build
	jar cf wc.jar -C build .

./build/:
	mkdir -p build/

clean:
	rm -rf build/
	rm wc.jar

run:
	./hadoop-3.4.1/bin/hadoop jar wc.jar Main /user/csf/input /user/csf/output

start:
	./hadoop-3.4.1/sbin/start-all.sh

stop:
	./hadoop-3.4.1/sbin/stop-all.sh

format:
	./hadoop-3.4.1/bin/hadoop namenode -format
