# Database Schema

## 1. Users
This collection will store information about the players and administrators.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `userId`         | String          | Unique identifier for the user (e.g., Firebase UID).                               |
| `username`       | String          | User's display name.                                                               |
| `email`          | String          | User's email address.                                                              |
| `role`           | String          | User role: either "admin" or "player".                                             |
| `teamId`         | String?         | Optional field, ID of the team the user belongs to.                                |
| `gamesPlayed`    | List<String>    | List of game IDs the user has participated in.                                     |

---

## 2. Teams
This collection will store team data for team-based gameplay.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `teamId`         | String          | Unique identifier for the team.                                                    |
| `teamName`       | String          | Name of the team.                                                                  |
| `members`        | List<String>    | List of userIds of team members.                                                   |
| `points`         | Integer         | Total points accumulated by the team.                                              |
| `zoneNow`         | Integer         | Which zone the team is now                                              |
| `startZone`         | Integer         | Which zone the team starts in                                              |

---

## 3. Games
Each game represents a city-specific discovery journey.

| **Field Name** | **Data Type**   | **Description**                                          |
|----------------|-----------------|----------------------------------------------------------|
| `gameId`       | String          | Unique identifier for the game.                          |
| `cityName`     | String          | City where the game takes place.                         |
| `description`  | String          | Description of the game.                                 |
| `zones`        | List<String>    | List of zone IDS in this game (sequence matters).        |
| `active`       | Boolean          | Current game status  |
| `startTime`    | Timestamp       | Start time of the game.                                  |
| `endTime`      | Timestamp       | End time of the game.                                    |
| `contact`      | String          | Whatsapp contact on the main organizer                   |


---

## 4. Zones
Each zone code represents a location with associated questions.

| **Field Name**      | **Data Type**   | **Description**                                                          |
|---------------------|-----------------|--------------------------------------------------------------------------|
| `locationName`      | String          | Name of the location (e.g., "Charles Bridge").                           |
| `hint`              | String?          | Hints where exactly in the location.                                     |
| `coordinates`       | GeoPoint        | Geographical coordinates (latitude, longitude) for the QR code location. |
| `mandatoryQuestion` | String      | The ID of the mandatory question associated with this QR code.           |
| `optionalQuestions` | List<String> | List of IDs of optional questions associated with this QR code.          |

---

## 5. Questions
Stores question data (mandatory, optional, and special questions).

| **Field Name**   | **Data Type**   | **Description**                                                                 |
|-------------------|-----------------|---------------------------------------------------------------------------------|
| `questionId`     | String          | Unique identifier for the question.                                            |
| `type`           | String          | Type of question: "text", "multichoice", "number", "image".                    |
| `questionText`   | String          | The actual question text.                                                      |
| `options`        | List<String>?   | Optional for multiple-choice questions.                                        |
| `correctAnswer`  | Any             | The correct answer (number, string, or option index).                          |
| `points`         | Integer         | Points awarded for correctly answering this question.                          |

---

## 6. Leaderboard
Tracks game progress and rankings.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `gameId`         | String          | The ID of the game.                                                                |
| `teamRankings`   | List<Map>       | A list of maps containing `teamId` and `points` for ranking.                       |
