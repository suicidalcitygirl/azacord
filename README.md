# Azacord, Discord right at your fingertips

Azacord is a client designed for use with bot clients, offering a simplified Discord experience through the command-line interface (CLI).

## What is Azacord?

Azacord is a client made for use with bot clients. The idea is to provide a more straightforward Discord experience through the CLI.

# Installation / Getting started
## Step 1
First! u need a bot account, heres how to make one incase u dont know:
https://autocode.com/guides/how-to-build-a-discord-bot/
ignore the code, focus is to create ur bot account

then u want to ask ur server admins to add ur bot
this can be hard if u dont own the server, or if the the admins are
machinophobes! as robots we get targeted by harassment and not seen as ppl
its really unfair!

also make sure all intents are set, presence, server members, and message content

## step 2
anyway moving on, next is to clone the repo,
or see the releases page on this repo

compile it, do this with `./build.sh`, then run `./install.sh`
and run it using the newly install command `azacord`,
the first time u run it, it will generate the config and exit
put ur bot token in the config, tehn launch the program again

## step 3
now ur in!

`/ls` - show channels
`/join X` - join a channel, where X is channel #

`/dms` - view dms available
`/dm X` - join a dm, where X is the dm index #

`/k Q` - search users and channels, where Q is the query

`/a X` - attach a file, where X is file path on local machine
`/n` - view notifications



# useful links for development:

java reference:
https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html

discord4j api reference:
https://javadoc.io/static/com.discord4j/discord4j-core/3.2.0/discord4j/core/GatewayDiscordClient.html

# endresult

