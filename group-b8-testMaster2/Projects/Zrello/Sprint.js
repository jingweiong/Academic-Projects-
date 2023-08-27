//global variables
var current_card_edit;
var all_card;

// set the minimum date
var today = new Date().toISOString().split('T')[0];

document.getElementsByName("setTodaysDate")[0].setAttribute('min', today);

function viewSprint(task){
    var sprintname = task.task_name
    var sprintdesc = task.description
    var sprintstart = task.start_date
    var sprintend = task.deadline
    
    // first clear the local storage
    // localStorage.clear()

    // set the data
    localStorage.setItem("sprintName", sprintname);
    localStorage.setItem("sprintDesc", sprintdesc);
    localStorage.setItem("sprintStart", sprintstart);
    localStorage.setItem("sprintEnd", sprintend);

    // switch to the sprint view page
    window.location.href = "sprintView.html";

    // updateSprintViewDetails(task.task_name, sprintdesc, sprintstart, sprintend);
}


// adds a member and the hours spent in the details list
function addSprintMemberView(member, hours){
    tableBody = document.getElementById("sprintMembersBody");
    newRow = document.createElement("tr");

        // create the header value
        newRowHeader = document.createElement("th");
        newRowHeader.innerHTML = member;

        // create the hour value
        newRowData = document.createElement("td");
        newRowData.innerHTML = hours;

        //append both to the new row
        newRow.append(newRowHeader, newRowData);
    
    tableBody.append(newRow);
}

// resets all the members in the sprint view page
function resetSprintMembersView(){
    document.getElementById("sprintMembersBody").innerHTML="";
}



// View Task Function
function viewTask(viewButton) {

    document.getElementsByClassName("addSuccessText")[0].remove();
    var card = viewButton.parentNode.parentNode.parentNode.parentNode;

    var elemArray = [];
    var labelArray = ["Sprint Name", "Description", "Status", "Start Date", "End Date"];
    var cardBody = card.children[1];

    elemArray.push(card.children[0].children[0].innerHTML); // Taskname
    elemArray.push(card.children[0].children[3].innerHTML); // Description
    elemArray.push(card.children[0].children[1].innerHTML); // Start Date
    elemArray.push(card.children[0].children[2].innerHTML); // deadline 
    elemArray.push(card.children[0].children[4].innerHTML); // status


    var viewBoxContainer = document.createElement('dl');
    viewBoxContainer.classList.add("container-fluid");

    var viewContainer = document.getElementById("viewContainer");

    var elemContainer, elemLabel, elemValue;
    for (let i = 0; i < elemArray.length; i++) {
        elemContainer = document.createElement("div");
        elemLabel = document.createElement("h5");
        elemValue = document.createElement("p");

        elemContainer.classList.add("row","justify-content-center","py-2","my-3");
        elemLabel.classList.add("col-1","rounded-pill","py-1");
        elemLabel.style.backgroundColor = "#FECD70";
        elemLabel.style.lineHeight = "100%";
        elemValue.classList.add("col-2","py-1");
        elemValue.style.fontSize = "18px";
        elemValue.style.lineHeight = "100%";

        elemLabel.innerHTML = labelArray[i];
        elemValue.innerHTML = elemArray[i];

        elemContainer.append(elemLabel,elemValue);
        viewBoxContainer.append(elemContainer);
    }

    viewContainer.append(viewBoxContainer);
    viewContainer.classList.remove("hide");

    var mainContainer = document.getElementById("mainContainer");
    mainContainer.classList.add("hide");

    var addButton = document.getElementById("addSprintButton");
    addButton.classList.add("hide");
}
// To hide the add success text 
function openModal() {
    document.getElementById("addSuccessText").classList.add("hide");
}

// to reset the button functions of the modal
function closeModal(){
    //change the save button to add and change the onclick
    document.getElementById("modalAddButton").textContent = "Add";
    document.getElementById("modalAddButton").onclick = function() { addSprint(this) };

    // Reset form values
    document.getElementById('taskName').value = "";
    document.getElementById('startDate').value = "";
    document.getElementById('deadline').value= "";
    document.getElementById('taskDesc').value= "";
    document.getElementById('statusDropdown').value= "Pending";
    
    
    // close the modal
    document.getElementById("addTaskCloseButton").click();
}

// Add Sprint Function
function addSprint(addButton) {

    let name = document.getElementById('taskName').value;
    let description = document.getElementById('taskDesc').value;
    let startDate = document.getElementById('startDate').value;
    let endDate = document.getElementById('deadline').value;
    let statusDropdown = document.getElementById('statusDropdown').value;

    const myJSON = {
        "task_name": name, 
        "description": description, 
        "start_date": startDate,
        "deadline": endDate,
        "status": statusDropdown
    }
    request("insert_sprint", myJSON);

    document.getElementById("addSuccessText").classList.remove("hide");

    // close the modal
    closeModal();
}

function editTask(editButton, id){
    
    //if checkSave is false, then the user wants to edit  
    // pop up the editing modal
    document.getElementById("addSprintButton").click();

    // get all the data of the current task
    card = editButton.parentNode.parentNode.parentNode.parentNode;
    all_card = editButton.parentNode.parentNode.parentNode.parentNode.parentNode;
    var elemArray = [];
    var labelArray = ["Task Name", "Description","Start Date", "Deadline", "Status"];

    elemArray.push(card.children[0].children[0].innerHTML); // Taskname
    elemArray.push(card.children[0].children[3].innerHTML); // Description
    elemArray.push(card.children[0].children[1].innerHTML); // Start Date
    elemArray.push(card.children[0].children[2].innerHTML); // deadline 
    elemArray.push(card.children[0].children[4].innerHTML); // status
    

    // pre fill the values in the edit form
    var myModal = document.querySelector("#sprintModal");

    myModal.querySelector('#taskName').value = elemArray[0];
    myModal.querySelector('#taskDesc').value = elemArray[1];
    myModal.querySelector('#startDate').value = elemArray[2];
    myModal.querySelector('#deadline').value = elemArray[3];
    myModal.querySelector('#statusDropdown').value = elemArray[4];
    

    //change the add button to save and change the onclick
    document.getElementById("modalAddButton").textContent = "Save";
    document.getElementById("modalAddButton").onclick = function() { saveTask(this, id) };
    
}

window.onload = function () {
    request("get_sprint").then((bl) => {
        console.log(bl)
    })
};

function saveTask(saveButton, id){
    var myModal = document.querySelector("#sprintModal");

        const myJSON = {
            "task_name": myModal.querySelector('#taskName').value,
            "description": myModal.querySelector('#taskDesc').value,
            "start_date": myModal.querySelector('#startDate').value,
            "deadline": myModal.querySelector('#deadline').value,
            "status": myModal.querySelector('#statusDropdown').value,
            "id": id
        }
        request("edit_sprint", myJSON);

    // close the modal
    closeModal();
}

function component(data){
    // reset all the data before adding them back in
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

    document.getElementById("taskCompleted").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="Completed";
    document.getElementById("taskCompleted").append(header);



    data.forEach(task => {
        // Create card element
    var card = document.createElement('div');
    card.className = "card"; //Apply card class

        // Create card header
    var cardHeader = document.createElement('div');
    var cardHeaderText = document.createElement('h5');
    cardHeaderText.innerHTML = task.task_name;
    cardHeader.classList.add("card-header","d-flex","flex-row","justify-content-center");
        // cardHeaderText.classList.add("col-6");

        // create card body
    var cardBody = document.createElement('div');
    cardBody.className = "card-body";

    // append Sprint Name
    var name = document.createElement('p');
    name.classList.add("card-text", "my-1");
    name.innerHTML = task.task_name;
    cardBody.append(name);

        //append start date
    var date1 = document.createElement('p');
    date1.classList.add("card-text", "my-1");
    date1.innerHTML = task.start_date;
    cardBody.append(date1);

        //append end date
    var date2 = document.createElement('p');
    date2.classList.add("card-text", "my-1");
    date2.innerHTML = task.deadline;
    cardBody.append(date2);
        
        // //append hidden inputs
    var desc = document.createElement('p');
    desc.className = "hiddenInput";
    desc.innerHTML = task.description;
    cardBody.append(desc);

    var status = document.createElement('p');
    status.className = "hiddenInput";
    status.innerHTML = task.status;
    cardBody.append(status);


        // task buttons list
    var taskButtonContainer = document.createElement('div');
    taskButtonContainer.className = "container-fluid";
    taskButtonContainer.id="taskButtonContainer";

            //buttons box div
        var buttonsRow = document.createElement('div');
        buttonsRow.classList.add("row", "justify-content-center");
        // <a  href=sprintView.html class="btn btn-dark">
            //view button
            var viewButton = document.createElement("a");
            viewButton.classList.add("col-2", "btn");
            viewButton.onclick=function(){ viewSprint(task) };

                //view icon
                var viewIcon = document.createElement('img');
                viewIcon.src = "assets/magnifying-glass-solid.svg";
                viewButton.append(viewIcon);
                
            buttonsRow.append(viewButton);

            // edit button
            var editButton = document.createElement("button");
            editButton.classList.add("col-2", "btn", "mx-3");
            editButton.onclick = function() { editTask(this, task.id); };

                //edit icon
                var editIcon = document.createElement('img');
                editIcon.src = "assets/pencil-solid.svg";
                editButton.append(editIcon);

            buttonsRow.append(editButton);

            //delete button
            var deleteButton = document.createElement("button");
            deleteButton.classList.add("col-2", "btn");
            deleteButton.onclick = function () { deleteSprint(this, task.id); };

                //delete icon
                var deleteIcon = document.createElement('img');
                deleteIcon.src = "assets/trash-solid.svg";
                deleteButton.append(deleteIcon);

            buttonsRow.append(deleteButton);

            


            


        taskButtonContainer.append(buttonsRow);

        
    cardBody.append(taskButtonContainer);

    card.append(cardBody);   

    var taskContainer;

    switch (task.status) {
        case "Pending":
            taskContainer = document.getElementById("taskPending");
            break;
        case "In Progress":
            taskContainer = document.getElementById("taskProgress");
            break;
        case "Completed":
            taskContainer = document.getElementById("taskCompleted");
            break;
        }

    taskContainer.append(card);

    });
}

function deleteSprint(delIcon, id) {

    request("delete_sprint", { "id": id })

    delIcon.parentNode.parentNode.parentNode.parentNode.remove();
    setTimeout(function () {
        alert("Task deleted");
    }, 200);

}




async function request(url, input = {}) {
    var xhr = new XMLHttpRequest();
    var status = false
    xhr.onreadystatechange = await function () {
        if (xhr.readyState == 4) {
            if (xhr.responseText != "" && xhr.responseText != null) {
                const data = JSON.parse(xhr.responseText)
                if (data.length > 0) {
                    if (url == "edit_sprint") {
                        var el = all_card.children[0]
                        all_card.innerHTML = ""
                        all_card.appendChild(el)
                    }
                    component(data)
                }
            }
        }

    }
    xhr.open('POST', 'http://127.0.0.1:8000/api/' + url);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(input));
}







    



