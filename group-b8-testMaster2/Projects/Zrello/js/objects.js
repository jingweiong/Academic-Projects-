"use strict"

class Task {

    constructor(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin) {
      this.primaryKey = primaryKey;
      this.name = name;
      this.description = description;
      this.assignedMember = assignedMember;
      this.storyPoints = storyPoints;
      this.status = status;
      this.startDate = startDate;
      this.endDate = endDate;
      this.tag = tag;
      this.priority = priority;
      this.taskOrigin = taskOrigin;
    }

    get name()
    {
        return this.name;   
    }
    get description()
    {
        return this.name;   
    }
    get assignedMember()
    {
        return this.name;   
    }
    get startDate()
    {
        return this.name;   
    }
    get endDate()
    {
        return this.name;   
    }


    set name(name)
    {
        this.name = name;   
    }
    set description(description)
    {
        this.description = description;   
    }
    set assignedMember(assignedMember)
    {
        this.assignedMember = assignedMember;   
    }
    set storyPoints(storyPoints)
    {
        this.storyPoints = storyPoints;
    }
    set status(status)
    {
        this.status = status;
    }
    set startDate(startDate)
    {
        this.startDate = startDate;   
    }
    set endDate(endDate)
    {
        this.endDate = endDate;   
    }
    set tag(tag)
    {
        this.tag = tag;
    }
    set priority(priority)
    {
        this.priority = priority;
    }
    set taskOrigin(taskOrigin)
    {
        this.taskOrigin = taskOrigin;
    }

    // Methods
    getTimeSpent(){
       var date1 = new Date(this.startdate);
       var date2 = new Date(this.enddate);
       var diffDays = Math.abs(date1 - date2) / 36e5;
       return diffDays;
    
    }

    fromData(taskData){
        this.name= taskData.name;
        this.person = taskData.person;
        this.startdate = taskData.startdate;
        this.enddate = taskData.enddate
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

        currentListLength = this._list.length

        return this._list[index];
    }

    
    //Method to add a booking into the appropriate list
    addTask(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)
    {
        let insertTask = new Task(primaryKey, name, description, assignedMember, storyPoints, status, endDate, tag, priority, taskOrigin)

        this._list.push(insertTask)

        currentListLength = this._list.length
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