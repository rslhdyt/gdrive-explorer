# gdrive-explorer
Google drive explorer application with Java spring boot

# how to install
1. clone this repo
2. import project to your IDE
3. install dependency with maven
4. create google account credentials in https://console.developer.google.com
5. put your credentials.json to `~/credentials` directory in your local environment or you can change directory in `GoogleDriveUtils.java` class

# how to use
1. access `/oauth/google/login` to authorize google
2. if authorization process success you'll redirected to `/google-drive`

# todo
- create testing
