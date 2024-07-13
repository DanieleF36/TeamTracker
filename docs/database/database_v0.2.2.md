# DATABASE CONFIGURATION
## **TABLES**
### *Attachment*
    This table represents an attachement to a specific task
- id string ***primary key***
- url string ***not null***
- creator string ***not null***, ***foreign key***
- date string ***not null***
    - format: YYYY-MM-DD
- task_id string ***not null***, ***foreign key***
- user_id string ***not null***, ***foreign key***

### *Comment*
    This table represents a comment to a specific task
- id sting ***primary key***
- message string ***not null***
- date string ***not null***
    - format: YYYY-MM-DD
- task_id string ***not null***, ***foreign key***
- user_id string ***not null***, ***foreign key***

### *Feedback*
    This table represents the relationship teams and users
- id_team string ***primary key***
- id_user string ***primary key***
- comment string ***not null***
- evaluation int ***not null***

### *History*
    This table represents a change done in a specific task
- id string ***primary key***
- type string ***not null***
    - this can be:
        - opened
        - closed
        - updated
        - time_updated
        - state_changed {old_state}->{new_state}
        - member_added: {name}
        - priority_changed {old_priority}->{new_priority}
        - due_date_changed {old_date}->{new_date}
        - attachement_added {name}
- message string ***not null***
- date string ***not null***
    - format: YYYY-MM-DD
- task_id string ***not null***, ***foreign key***

### *Kpi*
    This table represents a kpi
- id sting ***primary key***
- name string ***not null***
- value int ***not null***
- user_id string ***not null***, ***foreign key***

### *Message*
    This table represents a message
- id sting ***primary key***
- message string ***not null***
- date string ***not null***
    - format: YYYY-MM-DD
- team_id string ***foreign key***
    - if this is != null this is a team message and receiver have to be null
- sender string ***not null***, ***foreign key***
- receiver string ***foreign key***
    - if this is != null this is a personal message and team_id have to be null

### *Page*
    This table represents a page of a team
- id sting ***primary key***
- name string ***not null***
- position int ***not null***
- team_id string ***not null***, ***foreign key***

### *Tag*
    This table represents a tag of a task
- id sting ***primary key***
- name string ***not null***

### *Tags*
    This table represents the relationship between tags and tasks
- id_tag string ***primary key***, ***foreign key***
- id_task string ***primary key***, ***foreign key***

###  *Task*
    This table represents a task, with all the information needed
- id string ***primary key***
- name string ***not null***
- description string ***not null***
- due_date string ***not null***
    - format: YYYY-MM-DD
- creation_date string ***not null***
    - format: YYYY-MM-DD
- closing_date string ***not null***
    - format: YYYY-MM-DD
- state int ***not null***
    - says what page it is on
- creator string ***not null***, ***foreign key***
- closer string ***foreign key***
- priority int ***not null***
- time_spent float ***not null***
- team_id string ***not null***, ***foreign key***

### *Task_member*
    This table represents the relationship between user and tasks
- id_user string ***primary key***, ***foreign key***
- id_task string ***primary key***, ***foreign key***
- role string ***not null***

### *Team*
    This table represents a team
- id string ***primary key***
- name string ***not null***
- description string
- invitation_link string
- photo string
- creator string ***not null***, ***foreign key***
- creation_date string ***not null***
    - format: YYYY-MM-DD
- delivery_date string
    - this is the delivery date of the project
    - format: YYYY-MM-DD

### *Team_member*
    This table represents the relationship between teams and users
- id_team string ***primary key***
- id_user string ***primary key***
- role string ***not null***

### *User*
    This table represents a user
- id string ***primary key***
- nickname string ***not null***, ***unique***
- fullname string ***not null***
- email string ***not null***
- location string ***not null***
- phone int ***not null***
- description string ***not null***
- photo string 
- salt string ***not null***
- password string ***not null***