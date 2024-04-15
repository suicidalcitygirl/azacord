
# check if root lol
if [ "$(whoami)" != "root" ]; then

    echo "MUST BE RAN AS ROOT!!"
    echo "use sudo or something bro :D"
    exit -1;
fi

# make the necessary folder in /opt
mkdir -p /opt/azacord/

# copy the build result over
cp ./build/libs/azacord-*-all.jar /opt/azacord/azacord.jar

# create launch script
if [ -f /bin/azacord ]; then
    rm /bin/azacord
fi
echo "java -jar /opt/azacord/azacord.jar" > /bin/azacord
chmod 755 /bin/azacord
