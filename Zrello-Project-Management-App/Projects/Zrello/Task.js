class Task {

    constructor(name, description, assignedMember, startDate, endDate, storyPoints, status, tag, taskOrigin) {
      this.name = name;
      this.description = description
      this.assignedMember = assignedMember
      this.startDate = startDate;
      this.endDate = endDate;
      this.storyPoints = storyPoints;
      this.status = status
      this.tag = tag
      this.taskOrigin = taskOrigin
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

    get storyPoints(){ return this.storyPoints}
    get status(){return this.status}
    get tag(){return this.tag}
    get taskOrigin(){return this.taskOrigin}

    set storyPoints(storyPoints){this.storyPoints = storyPoints}
    set status(status){ this.status = status}
    set tag(tag){this.tag = tag}
    set taskOrigin(taskOrigin){this.taskOrigin = taskOrigin}

    set name(name)
    {
        this.name = name;   
    }
    set description(description)
    {
        this.description = this.description;   
    }
    set assignedMember(assignedMember)
    {
        this.assignedMember = assignedMember;   
    }
    set startDate(startDate)
    {
        this.startDate = startDate;   
    }
    set endDate(endDate)
    {
        this.endDate = endDate;   
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
