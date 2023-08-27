"use strict"
const APP_DATA_KEY = "zrelloAppData";
const PRODUCT_BACKLOG_DATA_LIST_KEY = "productBacklogData";
const SPRINT_LIST_DATA_KEY = "sprintListData";
const PRIMARY_KEY_DATA_KEY = "primaryKeyValue";
const SPRINT_PRIMARY_KEY_DATA_KEY = "sprintPrimaryKeyValue";


"use strict"

class Task {

    constructor(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin) {
      this._primaryKey = primaryKey;
      this._name = name;
      this._description = description;
      this._assignedMember = assignedMember;
      this._storyPoints = storyPoints;
      this._status = status;
    //   this._startDate = startDate;
      this._endDate = endDate;
      this._tag = tag;
      this._priority = priority;
      this._taskOrigin = taskOrigin;
    }

    get name()
    {
        return this._name;   
    }
    get description()
    {
        return this._description;   
    }
    get assignedMember()
    {
        return this._assignedMember;   
    }
    // get startDate()
    // {
    //     return this._startDate;   
    // }
    get endDate()
    {
        return this._endDate;   
    }


    set name(name)
    {
        this._name = name;   
    }
    set description(description)
    {
        this._description = description;   
    }
    set assignedMember(assignedMember)
    {
        this._assignedMember = assignedMember;   
    }
    set storyPoints(storyPoints)
    {
        this._storyPoints = storyPoints;
    }
    set status(status)
    {
        this._status = status;
    }
    // set startDate(startDate)
    // {
    //     this._startDate = startDate;   
    // }
    set endDate(endDate)
    {
        this_endDate = endDate;   
    }
    set tag(tag)
    {
        this._tag = tag;
    }
    set priority(priority)
    {
        this._priority = priority;
    }
    set taskOrigin(taskOrigin)
    {
        this._taskOrigin = taskOrigin;
    }

    // Methods
    getTimeSpent(){
       var date1 = new Date(this.startdate);
       var date2 = new Date(this.enddate);
       var diffDays = Math.abs(date1 - date2) / 36e5;
       return diffDays;
    
    }

    fromData(taskData){
        this._primaryKey = taskData._primaryKey;
        this._name= taskData._name;
        this._description = taskData._description;
        this._assignedMember = taskData._assignedMember;
        this._storyPoints = taskData._storyPoints
        this._status = taskData._status;
        // this._startDate = taskData._startDate;
        this._endDate = taskData._endDate;
        this._tag = taskData._tag;
        this._priority = taskData._priority;
        this._taskOrigin = taskData._taskOrigin;

    }
}

class List {

    constructor ()
    {
        this._startTime = new Date();
        this._list = [];
    }

    //Acessor to get Starting time of current session
    get startTime()
    {
        return this._startTime.toLocaleString();
    }
    //Acessor for list of bookings
    get list()
    {
        return this._list;
    }

    //Methods
    removeTask(index)
    {
        this._list.splice(index, 1);
    }
    
    getTask(index)
    {
        return this._list[index];
    }

    
    //Method to add a booking into the appropriate list
    addTask(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)
    {
        let insertTask = new Task(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)

        this._list.push(insertTask)

    }

    fromData(listData)
    {
        let data = listData._list
        console.log(data.length)
        this._list = []
        for ( let i =0; i<data.length; i++)
        {
           
            let task = new Task();
            task.fromData(data[i]);
            this._list.push(task);
            
        }
    }

}

class SprintList {

    constructor ()
    {
        this._timeNow = new Date();
        this._list = [];
    }

    //Acessor to get Starting time of current session
    get timeNow()
    {
        return this._startTime.toLocaleString();
    }
    //Acessor for list of bookings
    get list()
    {
        return this._list;
    }

    //Methods
    removeSprint(index)
    {
        this._list.splice(index, 1);
    }
    
    getSprint(index)
    {
        return this._list[index];
    }

    //Method to add a Sprint into the list
    addSprint(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)
    {
        let insertTask = new Task(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)

        this._list.push(insertTask)

    }

    fromData(listData)
    {
        let data = listData._list
        this._list = []
        for ( let i =0; i<data.length; i++)
        {
            let sprint = new Sprint()
            sprint.fromData(data[i]);
            this._list.push(sprint);
            
        }
    }

}


class Sprint{
    constructor(primaryKey, name, startDate, endDate, description){
        this._primaryKey = primaryKey;
        this._name = name;
        this._startDate = startDate;
        this._endDate = endDate;
        this._description = description;
        this._taskList = [];
        this._sprintStatus = "pending";
    }

    set primaryKey(primaryKey) {this._primaryKey = primaryKey;}
    set name(name) { this._name = name;}
    set startDate(startDate) {this._startDate = startDate;}
    set endDate(endDate) {this._endDate = endDate;}
    set description(description) {this._description = description;}

    changeEndDate(year, monthIndex, day){
        this.endDate = new Date(year, monthIndex, day)
    }

    changeStartDate(year, monthIndex, day){
        this.startDate = new Date(year, monthIndex, day)
    }

    getTimeSpent(){
        var date1 = new Date(this.startdate);
        var date2 = new Date(this.enddate);
        var diffDays = Math.abs(date1 - date2) / 36e5;
        return diffDays;
     }

    /**
     * @param {Task} task
     * @param {Map} map
     */
    deleteTask(task, map){
        map.delete(task.name)
     }

    /**
     * @param {Task} task
     * @param {Map} map
     */
    getTask(task, map){
        return map.get(task.name)
     }

    startSprint(){
        var currentDate = new Date();
	    if (this.startDate == currentDate){
            this.status = "in progress"
        }
        if (this.endDate == currentDate){
            this.status = "completed"
        }
    }

    // This method is used to add a task to the sprint's list
    addTask(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)
    {
        let insertTask = new Task(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)

        this._taskList.push(insertTask)
    }

    fromData(listData)
    {
        let data = listData._list
        console.log(data.length)
        this._taskList = []
        for ( let i =0; i<data.length; i++)
        {
           
            let task = new Task();
            task.fromData(data[i]);
            this._taskList.push(task);
            
        }
    }
}















//This function, checkForData(), checks if local storage with a specific key if data is present. Parameters used in this function is key
//if data is present then this function will return a true value else it will return a false value
function checkForData(key)
{
    if (localStorage.getItem(key) === null)
    {
        return false;
    }
    else 
    {
        return true;
    }
}

//Function updateLocalStorage() is used to update local storage with selected data at a specific key. Parameters used are key and data
function updateLocalStorage(key, data)
{
    let jsonString = JSON.stringify(data);

    localStorage.setItem(key, jsonString);
}

//Function getData() is used to get data from local storage with specific key. Parameters used are key
function getData(key)
{
    let jsonString = localStorage.getItem(key);
    let objectData = "hi <3"; 
    try
    {
        //Converting data from string form to original form
        objectData = JSON.parse(jsonString);
    }
    catch (error)
    {
        //Error message in case no data is detected
        console.log("no data");
    }
    finally
    {
        //Regardless of outcome, objectData should be returned
        return objectData;
    }
}

//Process that runs when the page loads
let taskListSession = new List();
let primaryKey = 1
//Checking if there is data in APP_DATA_KEY
if (checkForData(APP_DATA_KEY) === true)
{
    //If there is data in APP_DATA_KEY then data should be retrieved and made into an instance of Session class
    let objectData = getData(APP_DATA_KEY);
    taskListSession.fromData(objectData);
    //Identiy if there is a primary key already initiated and append it 
    primaryKey = getData(PRIMARY_KEY_DATA_KEY)
}
else
{
    //If no data is in APP_DATA_KEY local storage should be updated so that it contains an empty array
    updateLocalStorage(APP_DATA_KEY, taskListSession);
    updateLocalStorage(PRIMARY_KEY_DATA_KEY,primaryKey)
}




//======================================================
//Process that runs when the page loads
let productBacklogList = new List();
//Checking if there is data in PRODUCT_BACKLOG_DATA_LIST_KEY
if (checkForData(PRODUCT_BACKLOG_DATA_LIST_KEY) === true)
{
    //If there is data in PRODUCT_BACKLOG_DATA_LIST_KEY then data should be retrieved and made into an instance of Session class
    let objectData = getData(PRODUCT_BACKLOG_DATA_LIST_KEY);
    sprintList.fromData(objectData);
}
else
{
    //If no data is in PRODUCT_BACKLOG_DATA_LIST_KEY local storage should be updated so that it contains an empty array
    updateLocalStorage(PRODUCT_BACKLOG_DATA_LIST_KEY, sprintList);
}

//Process that runs when the page loads
let sprintList = new SprintList();
//Checking if there is data in SPRINT_LIST_DATA_KEY
if (checkForData(SPRINT_LIST_DATA_KEY) === true)
{
    //If there is data in SPRINT_LIST_DATA_KEY then data should be retrieved and made into an instance of Session class
    let objectData = getData(SPRINT_LIST_DATA_KEY);
    sprintList.fromData(objectData);
}
else
{
    //If no data is in SPRINT_LIST_DATA_KEY local storage should be updated so that it contains an empty array
    updateLocalStorage(SPRINT_LIST_DATA_KEY, taskListSession);
}

//Process that runs when the page loads
let taskPrimaryKey = 1;
//Checking if there is data in PRIMARY_KEY_DATA_KEY
if (checkForData(PRIMARY_KEY_DATA_KEY) === true)
{
    //If there is data in PRIMARY_KEY_DATA_KEY then data should be retrieved and made into an instance of Session class
    taskPrimaryKey = getData(PRIMARY_KEY_DATA_KEY)
}
else
{
    //If no data is in PRIMARY_KEY_DATA_KEY local storage should be updated so that it contains an empty array
    updateLocalStorage(PRIMARY_KEY_DATA_KEY, taskPrimaryKey);
}

//Process that runs when the page loads
let sprintPrimaryKey = 1;
//Checking if there is data in SPRINT_PRIMARY_KEY_DATA_KEY
if (checkForData(SPRINT_PRIMARY_KEY_DATA_KEY) === true)
{
    //If there is data in SPRINT_PRIMARY_KEY_DATA_KEY then data should be retrieved and made into an instance of Session class
    taskPrimaryKey = getData(SPRINT_PRIMARY_KEY_DATA_KEY)
}
else
{
    //If no data is in SPRINT_PRIMARY_KEY_DATA_KEY local storage should be updated so that it contains an empty array
    updateLocalStorage(SPRINT_PRIMARY_KEY_DATA_KEY, sprintPrimaryKey);
}
