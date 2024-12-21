build: src/*.java | ./build/
	./hadoop-3.4.1/bin/hadoop com.sun.tools.javac.Main src/WordCount.java -d build
	jar cf wc.jar -C build .

./build/:
	mkdir -p build/

clean:
	rm -rf build/
	rm wc.jar

run:
	./hadoop-3.4.1/bin/hadoop jar wc.jar WordCount /user/csf/input /user/csf/output
