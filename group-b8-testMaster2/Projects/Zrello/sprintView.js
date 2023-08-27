//global variables
var current_card_edit;
var all_card;
var currentTimeButton;

// set the minimum date
var today = new Date().toISOString().split('T')[0];
document.getElementsByName("setTodaysDate")[0].setAttribute('min', today);

// always update the sprint details when the page is opened
var newName = localStorage.getItem("sprintName");
var desc =localStorage.getItem("sprintDesc");
var start =localStorage.getItem("sprintStart");
var end =localStorage.getItem("sprintEnd");

// get the list of members 
var sprintMemberList = JSON.parse(localStorage.getItem("currentSprintMembers"));
var timeSpent = localStorage.getItem("currentSprintTotalTimeSpent");

if(sprintMemberList == null || timeSpent == null){
    updateSprintViewDetails(newName, desc, start, end, "0", "0");
}
else{
    let aveTime = parseInt(timeSpent) / sprintMemberList.length;
    updateSprintViewDetails(newName, desc, start, end, aveTime, timeSpent);
}


//=========================TESTING==========================//
// localStorage.setItem("john", JSON.stringify(["12", "13", "14"]))
// let hi = JSON.parse(localStorage.getItem("john"))
// hi.push("wow");
// hi[0] = 1;
// localStorage.setItem("john", hi);
// console.log(localStorage.getItem("john"))






// to go back to product backlog by hiding the view page
function goHome() {
    var viewContainer = document.getElementById("viewContainer");
    viewContainer.classList.add("hide");

    var mainContainer = document.getElementById("mainContainer");
    mainContainer.classList.remove("hide");

    var addButton = document.getElementById("addTaskButton");
    addButton.classList.remove("hide");
}

// Delete Task Function
// must change if add or remove any divs
function deleteTask(delIcon, id) {

    request("delete_task", { "id": id })

    delIcon.parentNode.parentNode.parentNode.parentNode.remove();
    setTimeout(function () {
        alert("Task deleted");
    }, 200);
}

// View Task Function
function viewTask(viewButton) {

    document.getElementById("viewContainer").innerHTML="";
    var card = viewButton.parentNode.parentNode.parentNode.parentNode;

    var elemArray = [];
    var labelArray = ["Task Name", "Description", "Story Points", "Tag", "Assigned To", "Deadline", "Status", "Priority", "Origin"];
    var cardBody = card.children[1];

    elemArray.push(card.children[0].children[0].innerHTML); // Taskname
    elemArray.push(cardBody.children[3].innerHTML); // Description
    elemArray.push(card.children[0].children[1].innerHTML); // storyPoints
    elemArray.push(cardBody.children[0].innerHTML); // tag
    elemArray.push(cardBody.children[1].innerHTML); // assignedto
    elemArray.push(cardBody.children[2].innerHTML); // date 
    elemArray.push(cardBody.children[4].innerHTML); // status
    elemArray.push(cardBody.children[5].innerHTML); // priority
    elemArray.push(cardBody.children[6].innerHTML); // origin


    var viewBoxContainer = document.createElement('dl');
    viewBoxContainer.classList.add("container-fluid");

    var viewContainer = document.getElementById("viewContainer");

    var elemContainer, elemLabel, elemValue;
    for (let i = 0; i < elemArray.length; i++) {
        elemContainer = document.createElement("div");
        elemLabel = document.createElement("h5");
        elemValue = document.createElement("p");

        elemContainer.classList.add("row", "justify-content-center", "py-2", "my-3");
        elemLabel.classList.add("col-1", "rounded-pill", "py-1");
        elemLabel.style.backgroundColor = "#FECD70";
        elemLabel.style.lineHeight = "100%";
        elemValue.classList.add("col-2", "py-1");
        elemValue.style.fontSize = "18px";
        elemValue.style.lineHeight = "100%";

        elemLabel.innerHTML = labelArray[i];
        elemValue.innerHTML = elemArray[i];

        elemContainer.append(elemLabel, elemValue);
        viewBoxContainer.append(elemContainer);
    }

    viewContainer.append(viewBoxContainer);
    viewContainer.classList.remove("hide");

    var mainContainer = document.getElementById("mainContainer");
    mainContainer.classList.add("hide");

    var addButton = document.getElementById("addTaskButton");
    addButton.classList.add("hide");
}

// To hide the add success text 
function openModal() {
    document.getElementById("addSuccessText").classList.add("hide");
}

// to reset the button functions of the modal
function closeModal() {
    //change the save button to add and change the onclick
    // document.getElementById("modalAddButton").textContent = "Add";
    // document.getElementById("modalAddButton").onclick = function () { addTask(this) };

    // Reset form values
    document.getElementById('taskName').value = "";
    document.getElementById('numberRange').value = "";
    document.getElementById('tagDropdown').value = "Frontend";
    document.getElementById('assignDropdown').value = "Avinash";
    document.getElementById('deadline').value = "";
    document.getElementById('taskDesc').value = "";
    document.getElementById('statusDropdown').value = "Pending";
    document.getElementById('originDropdown').value = "User Story";

    // close the modal
    document.getElementById("addTaskCloseButton").click();
}

async function request(url, input = {}) {
    var xhr = new XMLHttpRequest();
    var status = false
    xhr.onreadystatechange = await function () {
        if (xhr.readyState == 4) {
            if (xhr.responseText != "" && xhr.responseText != null) {
                const data = JSON.parse(xhr.responseText)
                if (data.length > 0) {
                    if (url == "get_sprint") {
                        viewSprint(data)
                    }
                    if (url == "get_member") {
                        viewMember(data)
                    }
                    if (url != "get_sprint" && url != "get_member") {
                        component(data)
                    }
                }
            }
        }

    }
    xhr.open('POST', 'http://127.0.0.1:8000/api/' + url);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(input));
}


window.onload = function () {
    request("get_task").then((bl) => {
        console.log(bl)
    })
};

// Add Task Function
function addTask(addButton) {
    //check the input validation then create the task
    result = inputValidation("#exampleModal");

    // if all fields are not empty then add the task
    if (!result) {

        // Creating object class 
        let name = document.getElementById('taskName').value;
        let description = document.getElementById('taskDesc').value;
        let assignedMember = document.getElementById('assignDropdown').value;
        let storyPoints = document.getElementById('numberRange').value;
        let statusD = document.getElementById('statusDropdown').value;
        let endDate = document.getElementById('deadline').value;
        let tagD = document.getElementById('tagDropdown').value;
        let priorityD = document.getElementById('priorityDropdown').value;
        let taskOrigin = document.getElementById('originDropdown').value;
        // taskListSession.addTask(primaryKey, name, description, assignedMember, storyPoints, statusD, endDate, tagD, priorityD, taskOrigin);
        //Local storage is updated with new data which was just inputted
        // updateLocalStorage(APP_DATA_KEY, taskListSession);
        // PRIMARY_KEY_DATA_KEY is updated with the new primary key for next 
        // primaryKey = primaryKey + 1
        // updateLocalStorage(PRIMARY_KEY_DATA_KEY,primaryKey)

        const myJSON = {
            "task_name": name, "Description": description, "Assign_To": assignedMember,
            "Story_Points": storyPoints, "Status": statusD, "Deadline": endDate, "Tag": tagD,
            "Priority": priorityD, "Task_Origin": taskOrigin
        }
        request("insert_task", myJSON);



        document.getElementById("addSuccessText").classList.remove("hide");

        // close the modal
        closeModal();
    }


}

function editTask(editButton, id, sprint_id) {

    //if checkSave is false, then the user wants to edit  
    // pop up the editing modal
    document.getElementById("addTaskButton").click();

    // get all the data of the current task
    current_card_edit = editButton.parentNode.parentNode.parentNode.parentNode;
    all_card = editButton.parentNode.parentNode.parentNode.parentNode.parentNode;

    var elemArray = [];
    var labelArray = ["Task Name", "Description", "Story Points", "Tag", "Assigned To", "Deadline", "Status", "Priority", "Origin"];
    var cardBody = current_card_edit.children[1];

    elemArray.push(current_card_edit.children[0].children[0].innerHTML); // Taskname
    elemArray.push(cardBody.children[3].innerHTML); // Description
    elemArray.push(current_card_edit.children[0].children[1].textContent); // storyPoints
    elemArray.push(cardBody.children[0].innerHTML); // tag
    elemArray.push(cardBody.children[1].innerHTML); // assignedto
    elemArray.push(cardBody.children[2].innerHTML); // date 
    elemArray.push(cardBody.children[4].innerHTML); // status
    elemArray.push(cardBody.children[5].innerHTML); // priority
    elemArray.push(cardBody.children[6].innerHTML); // origin

    // pre fill the values in the edit form
    var myModal = document.querySelector("#exampleModal");

    myModal.querySelector('#taskName').value = elemArray[0];
    myModal.querySelector('#taskDesc').value = elemArray[1];
    myModal.querySelector('#numberRange').value = elemArray[2];
    myModal.querySelector('#tagDropdown').value = elemArray[3];
    myModal.querySelector('#assignDropdown').value = elemArray[4];
    myModal.querySelector('#deadline').value = elemArray[5];
    myModal.querySelector('#statusDropdown').value = elemArray[6];
    myModal.querySelector('#priorityDropdown').value = elemArray[7];
    myModal.querySelector('#originDropdown').value = elemArray[8];
    myModal.querySelector('#sprintDropdown').value = sprint_id;

    //change the add button to save and change the onclick
    document.getElementById("modalAddButton").textContent = "Save";
    document.getElementById("modalAddButton").onclick = function () { saveTask(this, id) };

}

function saveTask(saveButton, id) {
    // check if the user entered data is valid

    result = inputValidation("#exampleModal");

    if (!result) {
        var myModal = document.querySelector("#exampleModal");

        const myJSON = {
            "task_name": myModal.querySelector('#taskName').value,
            "Description": myModal.querySelector('#taskDesc').value,
            "Assign_To": myModal.querySelector('#assignDropdown').value,
            "Story_Points": myModal.querySelector('#numberRange').value,
            "Status": myModal.querySelector('#statusDropdown').value,
            "Deadline": myModal.querySelector('#deadline').value,
            "Tag": myModal.querySelector('#tagDropdown').value,
            "Priority": myModal.querySelector('#priorityDropdown').value,
            "Task_Origin": myModal.querySelector('#originDropdown').value,
            "sprint_id": myModal.querySelector('#sprintDropdown').value,
            "id": id
        }
        request("edit_task", myJSON);


        // close the modal
        closeModal();
    }
}



function inputValidation(validationModal) {



    // carry out input validation for all fields
    var myModal = document.querySelector(validationModal);

    var err = myModal.querySelectorAll(".err");
    for (let i = 0; i < err.length; i++) {
        err[i].innerHTML = "";
    }


    var tname = myModal.querySelector('#taskName').value;
    var tdesc = myModal.querySelector('#taskDesc').value;
    var tnum = myModal.querySelector('#numberRange').value;
    var tdeadline = myModal.querySelector('#deadline').value;

    var checkEmpty = false;
    // if field is empty
    if (tname == '') {


        myModal.querySelector('#err-tname').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    }

    if (tdesc == '') {

        myModal.querySelector('#err-tdesc').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    }

    if (tnum == '') {

        myModal.querySelector('#err-tnum').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    } else {

        if (parseFloat(tnum) < 1 || parseFloat(tnum) > 10) {
            myModal.querySelector('#err-tnum').innerHTML = '<p class="text-danger mb-0">Only number between 1 and 10 is allowed</p>';
            checkEmpty = true;
        }

    }

    if (tdeadline == '') {

        myModal.querySelector('#err-tdeadline').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    }

    return checkEmpty;
}

function timeTrackingModalOpen(timeButton, sprint_id, task_id){
    
    currentTimeButton = timeButton;
    localStorage.setItem("task_id",sprint_id);
    document.getElementById("timeLogAddButton").onclick = function () { timeTrackingSave(sprint_id, task_id) };
    document.getElementById("openTimeLogButton").click();

    document.getElementById("dateTimeLog").value="";
    document.getElementById("hoursTimeLog").value="";
    
    
}   

function timeTrackingSave(sprint_id, task_id){
    dateRecorded = document.getElementById("dateTimeLog").value;
    hoursRecorded = document.getElementById("hoursTimeLog").value;
    let checkEmpty = false;

    var err = document.querySelectorAll(".err");
    for (let i = 0; i < err.length; i++) {
        err[i].innerHTML = "";
    }

    if (dateRecorded == '') {

        document.getElementById('err-tdeadline').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    } 
    else if(hoursRecorded == ''){
        document.getElementById('err-tnum').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;
    }
    else {

        if (parseFloat(hoursRecorded) < 0 ) {
            document.getElementById('err-tnum').innerHTML = '<p class="text-danger mb-0">Only positive integer is allowed</p>';
            checkEmpty = true;
        }

    }

    if(!checkEmpty){
        console.log(dateRecorded)
        console.log(hoursRecorded)

        var current_card_edit = currentTimeButton.parentNode.parentNode.parentNode.parentNode;
        console.log(current_card_edit)
        var cardBody = current_card_edit.children[1];

        // this wil save all the time logs each time it is added to the task
        // the data will be saved under the key of the member name
        // the value will be saved as a list
        // each value in the list will be in the following format
        // [date;time , date;time, date;time]

        // let timeString = dateRecorded.toString() + ";" + hoursRecorded.toString();
        // let member = localStorage.getItem(cardBody.children[1].innerHTML);
        // if(member == null){
        //     localStorage.setItem(cardBody.children[1].innerHTML, [])
        //     member = localStorage.getItem(cardBody.children[1].innerHTML);
        // }

        // member.push(timeString);
        // localStorage.setItem(cardBody.children[1].innerHTML, member);


        const myJSON = {
            "task_name": current_card_edit.children[0].children[0].innerHTML,// Taskname
            "Description":cardBody.children[3].innerHTML, // Description
            "Assign_To": cardBody.children[1].innerHTML,// assignedto
            "Story_Points": current_card_edit.children[0].children[1].textContent, // storyPoints
            "Status": cardBody.children[4].innerHTML,// status,
            "Deadline":cardBody.children[2].innerHTML,// date 
            "Tag": cardBody.children[0].innerHTML, // tag
            "Priority": cardBody.children[5].innerHTML, // priority,
            "Task_Origin": cardBody.children[6].innerHTML,// origin
            "datetimelog": document.getElementById("dateTimeLog").value,
            "hourstimelog": document.getElementById("hoursTimeLog").value,
            "sprint_id": sprint_id,
            "id": task_id
        }
        request("edit_task", myJSON);

        // HERE YOU NEED TO ADD THE DATABASE STUFF OF WHERE TO SAVE THESE DATA

        document.getElementById("timeLogModalCloseButton").click();
    }    
}

function component(data) {
    let sprintMemberList = [];
    let totalHours = 0;
    let sprintTimeLoggedList = [];

    // reset the time logged for all members
    let allMemberList = JSON.parse(localStorage.getItem("memberList"));
    if(!(allMemberList==null)){
        for (var i = 0; i < allMemberList.length; i++) { 
            localStorage.setItem(allMemberList[i], JSON.stringify([]))
        }
    }

    // reset all the columns before adding in the data again
    document.getElementById("taskPending").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="Pending";
    document.getElementById("taskPending").append(header);

    document.getElementById("taskProgress").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="In Progress";
    document.getElementById("taskProgress").append(header);

    document.getElementById("taskReview").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="Peer Review";
    document.getElementById("taskReview").append(header);

    document.getElementById("taskTesting").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="Testing";
    document.getElementById("taskTesting").append(header);

    document.getElementById("taskCompleted").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="Completed";
    document.getElementById("taskCompleted").append(header);
    
    
    
    data.forEach(task => {
        // get the list of dates and hours logged for this task
        let datesLoggedList = (task.datetimelog); 
        let hoursLoggedList = (task.hourstimelog);

        if(datesLoggedList == null || hoursLoggedList == null){
            var dateList = false
        }
        else{
            var dateList = true
            datesLoggedList=datesLoggedList.split(",")
            hoursLoggedList=hoursLoggedList.split(",")
        }

        // get the current list of time tracked for this member
        let currentMemberTimeTrack = JSON.parse(localStorage.getItem(task.Assign_To));
        if (currentMemberTimeTrack==null){
            currentMemberTimeTrack=[];
        }

        // for each task, create a string of date and hours and push to the current member time tracked list
        if(dateList){
            for (var i = 0; i < datesLoggedList.length; i++) { 
                let stringSave = datesLoggedList[i] + ";" + hoursLoggedList[i] 
                currentMemberTimeTrack.push(stringSave)
            }
        }
        // save the latest time track data
        localStorage.setItem(task.Assign_To, JSON.stringify(currentMemberTimeTrack));

         



        // this will check if the task is to be displayed under this sprint
        if (task.sprint_id == newName) {
            // add this member to the list of members for this sprint
            if(!(task.Assign_To in sprintMemberList)){
                sprintMemberList.push(task.Assign_To);
            }

            if(dateList){
                // add this time log to the list if there are any time logs
                for (var i = 0; i < datesLoggedList.length; i++) { 
                    let stringSave = datesLoggedList[i] + ";" + hoursLoggedList[i] 
                    sprintTimeLoggedList.push(stringSave)

                    // total the time logged for this sprint
                    totalHours += parseInt(hoursLoggedList[i]);
                }
            }

            var card = document.createElement('div');
            card.className = "card"; //Apply card class
    
            // Create card header
            var cardHeader = document.createElement('div');
            var cardHeaderText = document.createElement('h5');
            cardHeaderText.innerHTML = task.task_name;
            cardHeader.classList.add("card-header", "d-flex", "flex-row", "justify-content-center");
            // cardHeaderText.classList.add("col-6");
    
            cardHeader.append(cardHeaderText);
    
            // Append span badge 
            var badgeCont = document.createElement('label');
            var badge = document.createElement('span');
            badge.innerHTML = task.Story_Points;
            badgeCont.classList.add("badge", "bg-secondary", "mx-3", "rounded-circle");
            badge.classList.add("align-middle", "p-1");
    
            if (task.Story_Points) {
                badgeCont.append(badge);
            }
            cardHeader.append(badgeCont);
            card.append(cardHeader);
    
            // create card body
            var cardBody = document.createElement('div');
            cardBody.className = "card-body";
    
            // append span tag badge
            var tag = document.createElement('span');
            tag.classList.add('badge', 'card-text', 'bg-secondary');
            tag.innerHTML = task.Tag;
            cardBody.append(tag);
    
            //append assigned to 
            var assignedTo = document.createElement('p');
            assignedTo.classList.add("card-text", "my-1");
            assignedTo.innerHTML = task.Assign_To;
            cardBody.append(assignedTo);
    
            //append task date
            var date = document.createElement('p');
            date.classList.add("card-text", "my-1");
            date.innerHTML = task.Deadline;
            cardBody.append(date);
    
            // //append hidden inputs
            var desc = document.createElement('p');
            desc.className = "hiddenInput";
            desc.innerHTML = task.Description;
            cardBody.append(desc);
    
            var status = document.createElement('p');
            status.className = "hiddenInput";
            status.innerHTML = task.Status;
            cardBody.append(status);
    
            var priority = document.createElement('p');
            priority.className = "hiddenInput";
            priority.innerHTML = task.Priority;
            cardBody.append(priority);
    
            var origin = document.createElement('p');
            origin.className = "hiddenInput";
            origin.innerHTML = task.Task_Origin;
            cardBody.append(origin);
    
            // task buttons list
            var taskButtonContainer = document.createElement('div');
            taskButtonContainer.className = "container-fluid";
            taskButtonContainer.id = "taskButtonContainer";
    
            //buttons box div
            var buttonsRow = document.createElement('div');
            buttonsRow.classList.add("row", "justify-content-center");
    
            //view button
            var viewButton = document.createElement("button");
            viewButton.classList.add("col-2", "btn");
            viewButton.onclick = function () { viewTask(this); };
    
            //view icon
            var viewIcon = document.createElement('img');
            viewIcon.src = "assets/magnifying-glass-solid.svg";
            viewButton.append(viewIcon);
    
            buttonsRow.append(viewButton);
    
            // edit button
            var editButton = document.createElement("button");
            editButton.classList.add("col-2", "btn", "mx-3");
            editButton.onclick = function () { editTask(this, task.id, task.sprint_id); };
    
            //edit icon
            var editIcon = document.createElement('img');
            editIcon.src = "assets/pencil-solid.svg";
            editButton.append(editIcon);
    
            buttonsRow.append(editButton);
    
            //delete button
            var deleteButton = document.createElement("button");
            deleteButton.classList.add("col-2", "btn");
            deleteButton.onclick = function () { deleteTask(this, task.id); };
    
            //delete icon
            var deleteIcon = document.createElement('img');
            deleteIcon.src = "assets/trash-solid.svg";
            deleteButton.append(deleteIcon);
    
            buttonsRow.append(deleteButton);

            //time_tracking_
            var timeButton = document.createElement("button");
            timeButton.classList.add("col-2","btn", "mx-3");
            timeButton.onclick = function () { timeTrackingModalOpen(this, task.sprint_id, task.id);};
            
            
                //time_tracking_icon
                var time_tracking_icon = document.createElement('img');
                time_tracking_icon.src = "assets/clock-solid.svg";
                timeButton.append(time_tracking_icon);
            
            buttonsRow.append(timeButton)

    
            taskButtonContainer.append(buttonsRow);
    
    
            cardBody.append(taskButtonContainer);
    
            // Color coding the card based on priority level
            switch (task.Priority) {
                case "Critical":
                    card.style.backgroundColor = "pink";
                    break;
                case "High":
                    card.style.backgroundColor = "#E6B566";
                    break;
                case "Medium":
                    card.style.backgroundColor = "yellow";
                    break;
                case "Low":
                    card.style.backgroundColor = "lightgreen";
                    break;
            }
    
            card.append(cardBody);
    
            console.log(status)
    
            var taskContainer; 
    
            switch (task.Status) {
                case "Pending":
                    taskContainer = document.getElementById("taskPending");
                    break;
                case "In Progress":
                    taskContainer = document.getElementById("taskProgress");
                    break;
                case "Peer Review":
                    taskContainer = document.getElementById("taskReview");
                    break;
                case "Testing":
                    taskContainer = document.getElementById("taskTesting");
                    break;
                case "Completed":
                    taskContainer = document.getElementById("taskCompleted");
                    break;
            }
    
            taskContainer.append(card);
        
        }});
    localStorage.setItem("currentSprintMembers", JSON.stringify(sprintMemberList));
    localStorage.setItem("currentSprintTotalTimeSpent", totalHours);
    localStorage.setItem("sprintTimeList", JSON.stringify(sprintTimeLoggedList))

    // update the sprint details each time the other component details change
    let sprintMembers = JSON.parse(localStorage.getItem("currentSprintMembers"));
    let timeSpent = localStorage.getItem("currentSprintTotalTimeSpent");

    if(sprintMembers == null || timeSpent == null || sprintMembers==[]){
        updateSprintViewDetails(newName, desc, start, end, "0", "0");
    }
    else{
        if(sprintMembers.length>0){
            var aveTime = parseInt(timeSpent) / (sprintMembers.length);
        }else{
            var aveTime = 0
        }
        updateSprintViewDetails(newName, desc, start, end, aveTime, timeSpent);
    }
        
}

function outputAverageTime(){
    // get the input for the start and end dates and check that it is not empty
    let startDate = (document.getElementById("startDate").value).toString();
    let endDate = (document.getElementById("endDate").value).toString();
    let check = false
    
    if(startDate == '' || endDate == '' ){
        document.getElementById("err-tdeadline").innerHTML='<p class="text-danger mb-0">The dates cannot be empty</p>';
    }
    else{
        document.getElementById("err-tdeadline").innerHTML='';
        
        // check for start date before end date
        if(startDate<endDate){
            check = true
        }
        else{
            document.getElementById("err-tdeadline").innerHTML='<p class="text-danger mb-0">Start date must be before end</p>';
        }
    }

    if(check){
        let totalTimeNow = 0;

        let timeRecorded = JSON.parse(localStorage.getItem("sprintTimeList"));
        
        // date;hours list
        // iterate through the list of time logged for this sprint and add it to the total hours if within the range
        if (!(timeRecorded==null)){
            for(var j=0; j<timeRecorded.length; j++){
                let date = timeRecorded[j].split(";")[0]
                let hour = timeRecorded[j].split(";")[1]

                // check if the date is within the range
                if (date>=startDate && date<=endDate){
                    totalTimeNow+=parseInt(hour);
                }
            }
        }
        let Difference_In_Time = new Date(endDate).getTime() - new Date(startDate).getTime();
  
        // To calculate the no. of days between two dates
        let Difference_In_Days = Difference_In_Time / (1000 * 3600 * 24)

        console.log(Difference_In_Days)
        document.getElementById("averageTimeSprint").innerHTML=(totalTimeNow/Difference_In_Days)+" hours per day";
    }
    
    
    

}


// fills up the details in the sprintView html
function updateSprintViewDetails(sprintname, sprintdesc,sprintstart, sprintend, avetime, totaltime){
    // update the name
    document.getElementById("sprintName").innerHTML=sprintname

    // update desc
    document.getElementById("sprintDesc").innerHTML=sprintdesc

    // update start date
    document.getElementById("sprintStart").innerHTML=sprintstart

    // update end date
    document.getElementById("sprintEnd").innerHTML=sprintend

    // // update average time
    document.getElementById("sprintAveTime").innerHTML=avetime

    // // update total time
    document.getElementById("sprintTotalTime").innerHTML=totaltime
}

// localStorage.setItem("list", "2022-10-19,2021-07-19,2022-01-19,1923-10-19")
// localStorage.setItem("listhour", "1,2,3,4")
// let hoursLoggedList = localStorage.getItem("listhour").split(",")
// let datesLoggedList = localStorage.getItem("list").split(",");
// console.log(localStorage.getItem("list").split(","));
// // for(let i=0; i<datesLoggedList.length; i++){
// //     console.log(new Date(localStorage.getItem("list").split(",")[i]))
// // }

// let memberTimeTrack = [];

// for (var i = 0; i < datesLoggedList.length; i++) { 
//     let stringSave = datesLoggedList[i] + ";" + hoursLoggedList[i] 
//     console.log(hoursLoggedList[i] )
//     memberTimeTrack.push(stringSave);
// }

// console.log(memberTimeTrack)

// // localStorage.removeItem("task.Assign_To");
// localStorage.setItem("task.Assign_To", JSON.stringify(memberTimeTrack));
// // console.log(JSON.parse(localStorage.getItem("task.Assign_To")));

// let currentList = [1, 2, 3, 4]
// currentList.push("2")

// let latest = JSON.parse(localStorage.getItem("task.Assign_To"))
// latest.push(currentList)
// console.log(latest)

// localStorage.setItem("hi", JSON.stringify([]))
// let list = JSON.parse(localStorage.getItem("hi"))
// list.push("wow")
// list.push("hey")
// console.log(list)



// xs=[1, 2, 3]
// for (var i = 0; i < xs.length; i++) { 
//     console.log(xs[i]); 
// }


function sprintOnClick(){
    request("get_sprint");
}


function viewSprint(data){
    var assignSprint = document.getElementById('sprintDropdown')

    // before adding in all the sprints always reset
    assignSprint.innerHTML=""

    // add in the default sprint (unassigned)
    var sprintHeader = document.createElement('option')
    sprintHeader.innerHTML = "Unassigned sprint"
    assignSprint.append(sprintHeader);

    if (data != null){
        data.forEach(sprint => {
            var sprintHeader = document.createElement('option')
            sprintHeader.value += sprint.task_name
            sprintHeader.innerHTML = sprint.task_name

            assignSprint.append(sprintHeader);
        })
    } else {
        assignSprint.innerHTML = "null"
    }
}

function viewMember(data){

   
    var assignMember = document.getElementById('assignDropdown')
    assignMember.innerHTML="";

    data.forEach(member => {
        var memberHeader = document.createElement('option')
        memberHeader.innerHTML = member.name

        assignMember.append(memberHeader);
    })
}

function memberOnClick(){
    request("get_member")
}
