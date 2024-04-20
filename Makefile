
build: ./src/main/java/scs/*
	rm -rvf ./build/libs/*
	clear;./gradlew build

run:
	java -jar ./build/libs/azacord-*-all.jar
