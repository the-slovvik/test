# How to run the app

* ```./gradlew bootRun -Pargs={path to file}```
* ```example: ./gradlew bootRun -Pargs=/home/logs.json```

# What can be improved
* Not tests due to lack of time
* Concurrent/parallel processing

# Faced problems
* How to split a file if line is not fixed size?
* Due to first bullet point not parallel processing of file was introduced
* The bottleneck at small files was reading and writing the file
* With the bigger files like 1 GB it seems that file based HSQLDB was the problem - not sure. 

