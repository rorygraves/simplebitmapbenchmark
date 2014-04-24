mkdir -p bin
javac  -sourcepath ./src/main/java -classpath .:lib/* -d bin ./src/main/java/bitmapbenchmarks/synth/Benchmark.java
java -server -cp bin:lib/* bitmapbenchmarks.synth.Benchmark
