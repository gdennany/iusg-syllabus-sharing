# IUSG Congress Bill Dashboarding Site

## How to check out and run
1. Make sure IntelliJ IDEA, Git, and jdk 8 (1.8) are installed
  - JDK 8 download: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html
    - to see your current JDK, run: `git --version` from the command line
    - Alternatively you can set the JDK for this project on intellij. In intellij, go to File -> Project Structure -> Project Tab -> Project SDK -> select 1.8
2. If not already, download docker for your OS.
  - Docker Download: https://docs.docker.com/get-docker/
3. (Windows users only) If you do not have Windows 10 Pro, you need to use IUWare to get a Windows EDU activation key and use it in your activation settings.
4. Make sure Linux containers are being used.
  - These should be defaulted on in docker. They are for Mac and Windows.
5. Bring up a local docker database:
  - From the command line, run: `docker run -d --name iusg-syllabus -p 8080:8080 -p 8081:8081 -p 28015:28015 -p 29015:29015 rethinkdb`
6. Set environment variables (database_hostname=localhost;url_base=localhost;cleanse=true;dbpassword=rethinkdb)
  - Click the green run button on the main method (in CongressDashboard.kt), then click on "Edit (or Create) CongressDashboardKt" configuration.
    - Under the configuration tab, in the environment variables section, add: `database_hostname=localhost;url_base=localhost;cleanse=true;dbpassword=rethinkdb`
    - Click apply, then okay.
  - Specifics of above command:
    - `database_hostname` - localhost if running locally
    - `url_base` - localhost if running locally
    - `cleanse` - whether to remove all data first
    - `dbpassword` - rethinkdb password if running in prod or staging
7. Now run the CongressDashboard.kt main method.
8. Navigate to http://localhost/ in a web browser.
  - Here you should see a web response. If you receive an error then you did something wrong.
  - To see your local changes, click th restart button in intellij (or re-run the main method) and refresh your web browser.
9. Happy developing
