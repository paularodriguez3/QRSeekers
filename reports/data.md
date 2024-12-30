# Database Schema

## 1. Users
This collection will store information about the players and administrators.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `id`             | String          | Primary Key, unique identifier for the user.                                       |
| `nickname`       | String          | User's display name.                                                               |
| `email`          | String          | User's email address, default "None".                                             |
| `zone`           | String          | Foreign Key referencing Zone ID, default "None".                                   |
| `points`         | Integer         | Total points accumulated by the user.                                              |
| `participates`   | String          | Foreign Key referencing Game ID.                                                   |
| `gameName`       | String          | Name of the game the user is participating in, default "None".                    |

---

## 2. Games
This collection will store information about the games available to users.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `gameId`         | String          | Unique identifier for the game.                                                    |
| `name`           | String          | Name of the game.                                                                  |
| `description`    | String          | Short description of the game.                                                     |
| `imageRes`       | String          | URL or resource name for the game image.                                           |
| `color`          | Long            | Hex color code for the game.                                                       |
| `zones`          | List<String>    | List of zone IDs associated with this game.                                        |

---

## 3. Zones
This collection will store information about the zones within a game.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `zoneId`         | String          | Unique identifier for the zone.                                                    |
| `name`           | String          | Name of the zone.                                                                  |
| `hint`           | String?         | Optional hint for finding the QR code in the zone.                                 |
| `questions`      | List<String>?   | Optional list of question IDs associated with this zone.                           |
| `passkey`        | String          | Passkey required to unlock or access the zone.                                     |

---

## 4. Questions
This collection will store the questions associated with zones.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `questionId`     | String          | Unique identifier for the question.                                                |
| `text`           | String          | Text of the question.                                                              |
| `type`           | String          | Type of question (e.g., "multi" or "text").                                        |
| `points`         | Int             | Number of points awarded for the correct answer.                                   |
| `options`        | List<String>?   | Optional list of options for multiple-choice questions.                            |
| `correctAnswer`  | Any?            | Correct answer to the question.                                                    |
| `imageUrl`       | String?         | Optional URL for an image associated with the question.                            |

---


# Yet to implement


## 5. Teams
This collection will store information about teams formed by users.

| **Field Name**   | **Data Type**   | **Description**                                                                     |
|-------------------|-----------------|-------------------------------------------------------------------------------------|
| `teamId`         | String          | Unique identifier for the team.                                                    |
| `name`           | String          | Team's name.                                                                       |
| `members`        | List<String>    | List of user IDs who are members of the team.                                      |
| `gameId`         | String          | ID of the game the team is participating in.                                       |
| `points`         | Int             | Total points accumulated by the team.                                              |

---

## Relationships

1. **Users and Teams**:
   - A user can belong to one team (optional).
   - A team can have multiple users.

2. **Games and Zones**:
   - A game can have multiple zones.
   - A zone belongs to a single game.

3. **Zones and Questions**:
   - A zone can have multiple questions (optional).
   - A question belongs to a single zone.

4. **Users and Games**:
   - A user can participate in multiple games.
   - Games track participating users indirectly through teams or individual progress.

