mkdir -p bin
javac  -sourcepath ./src/main/java -classpath .:lib/* -d bin ./src/main/java/bitmapbenchmarks/synth/Benchmark.java
java -server -javaagent:lib/object-explorer.jar -cp bin:lib/* bitmapbenchmarks.synth.Benchmark
