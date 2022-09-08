How do we scale the solution?
1. Adding more Sports? MLB, NHL, NBA

The db structure has been set up so that more sports can be added in and utilise the basic structure of Players / Positions / PlayerPositions. 
These structures are sport agnostic.
 
3. Adding all the NFL teams?
   
Again, the db structure allows for teams to be added into a league, and leagues to be grouped under a specific sport umbrella.
I have also included a squad entity so that teams can track depth chart's across seasons

How do you go about testing your solution?
1. Are you handling all the various edge cases correctly?
2. Can you add any automated unit tests to your solution?
   
Unit cases have been added for 27 scenarios - please see included html file [test cases](Test%20Results%20-%20DepthChartServiceTest.html).
The test file also includes log output.
I would have liked to implemented Cucumber blackbox tests for BDD, but ran short of time.


To build and run code:

The project is a Spring boot app built and run with Maven. I have included an IntelliJ run configuration in the project,
so once you have imported the maven project, you can run it utilizing that.

Alternatively, you can run the code from the command line with the command `./mvnw spring-boot:run`

To test the code once started, I have included a [postman collection](src/main/resources/FanDuel.postman_collection.json) 
with all the test steps from the challenge defined and ordered.

The app is using embedded H2 file-based storage, which will store the db data in the project root dir ./fanduel/db.
If you wish to view the db during assesment, it can be accessed in a browser at [this url](http://localhost:8080/fan-duel/trading/solution/h2-console)

I did send through the below queries but did not get a response, so I have added my assumptions:

1. It says in the description "You can also assume that a number within the team uniquely identifies that player.".
With that in mind, are the method signatures in the use cases strict definitions, or can I simply use the player number in place of the "player" object?
addPlayerToDepthChart(position, player, position_depth)
removePlayerFromDepthChart(position, player)
getBackups(position, player)

I have used the player number only.

2. For removing the player from the depth chart, the two conditions in the use case have different return types (see below). 
Can the second point instead be something like handle appropriately / return empty object / return null? 
Or should the first point be to return a single element list of the removed player?
removePlayerFromDepthChart(position, player)
Removes a player from the depth chart for a given position and returns that player
An empty list should be returned if that player is not listed in the depth chart at that position

I have returned null from the service to the controller which in turn returns a 400 response.

3. For adding the player to the depth chart, in the use case where for example the requested position_depth is 2, but there is no player at depth 0 or 1. 
Is the position_depth of 2 overridden to be 0 or should they still be inserted at position depth 2?

I have ensured that there are no gaps in the depth chart, by overriding the position depth if there are no entries.

4. For the sample data, based on the output I assumed:
getBackups(“QB”, JaelonDarden) / Output / #10 – Scott Miller
Should be:
getBackups(“LWR”, JaelonDarden) / Output / #10 – Scott Miller