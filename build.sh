
echo "CLEANING UP OLD BUILDS, IF ANY"
# clean up old builds, if any
rm -rvf ./build/libs/*

echo "BUILDING PROJECT WITH GRADLE"
# build the project
./gradlew build
