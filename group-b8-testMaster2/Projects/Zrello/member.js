//global variables
var current_card_edit;
var all_card;
// set the minimum date
// var today = new Date().toISOString().split('T')[0];
// document.getElementsByName("setTodaysDate")[0].setAttribute('min', today);

// to go back to product backlog by hiding the view page
function goHome() {
    var viewContainer = document.getElementById("viewContainer");
    viewContainer.classList.add("hide");

    var mainContainer = document.getElementById("mainContainer");
    mainContainer.classList.remove("hide");

    var addButton = document.getElementById("addMemberButton");
    addButton.classList.remove("hide");
}


// Delete Member Function
// must change if add or remove any divs
function deleteMember(delIcon, id) {

    request("delete_member", { "id": id })

    delIcon.parentNode.parentNode.parentNode.parentNode.remove();
    setTimeout(function () {
        alert("Member deleted");
    }, 200);

}


// to fill the time data table
function updateTimeLogTable(totalTime, averageTime){
    document.getElementById("totalTimeLogged").innerHTML=totalTime
    document.getElementById("averageTimePerMember").innerHTML=averageTime
}



// // to add time hours spent data for a member
// function addMemberTimeData(memberName, timeList){
//     // create a new row
//     var row = document.createElement("tr");

//         // list must have 8 items
//         var member = document.createElement("th");
//         member.innerHTML=memberName;
//         row.append(member);

//         // append the date hours
//         for(let i = 0; i < timeList.length; i++){
//             var hour = document.createElement("td");
//             hour.innerHTML=timeList[i];
//             row.append(hour);
//         }
    
//     // append the row to the table
//     document.getElementById("memberTimeData").append(row);
// }

// function resetMemberTimeData(){
//     document.getElementById("memberTimeData").innerHTML="";
// }





// View Member Function
function viewMember(viewButton) {

    document.getElementsByClassName("addSuccessText")[0].remove();
    var card = viewButton.parentNode.parentNode.parentNode.parentNode;

    var elemArray = [];
    var labelArray = ["Member Name", "email"];
    // var labelArray = ["Task Name", "Description", "Story Points", "Tag", "Assigned To", "Deadline", "Status", "Priority", "Origin"];
    var cardBody = card.children[1];

    elemArray.push(card.children[0].children[0].innerHTML); // Membername
    elemArray.push(cardBody.children[3].innerHTML); // email


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

    var addButton = document.getElementById("addMemberButton");
    addButton.classList.add("hide");
}

// To hide the add success text 
function openModal() {
    document.getElementById("addSuccessText").classList.add("hide");
}

// to reset the button functions of the modal
function closeModal() {
    //change the save button to add and change the onclick
    document.getElementById("modalAddButton").textContent = "Add";
    document.getElementById("modalAddButton").onclick = function () { addMember(this) };

    // Reset form values
    document.getElementById('memberName').value = "";
    document.getElementById('email').value = "";

    // close the modal
    document.getElementById("addMemberCloseButton").click();
}

async function request(url, input = {}) {
    var xhr = new XMLHttpRequest();
    var status = false
    xhr.onreadystatechange = await function () {
        if (xhr.readyState == 4) {
            if (xhr.responseText != "" && xhr.responseText != null) {
                const data = JSON.parse(xhr.responseText)
                if (data.length > 0) {
                    //=========================NEEDS ACTION=====================================
                    // if (url == "edit_member" || url == "insert_member") {
                    //     var el = all_card.children[0]
                    //     all_card.innerHTML = ""
                    //     all_card.appendChild(el)
                    // }
                    component(data)
                }
            }
        }

    }
    xhr.open('POST', 'http://127.0.0.1:8000/api/' + url);
    xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
    xhr.send(JSON.stringify(input));
}

/**
 *  NEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEDS ACTIOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOOON
 */
window.onload = function () {
    request("get_member").then((bl) => {
        console.log(bl)
    })
};

// Add Member Function
function addMember(addButton) {
    //check the input validation then create the member
    result = inputValidation("#exampleModal");

    // if all fields are not empty then add the member
    if (!result) {

        // Creating object class 
        let name = document.getElementById('memberName').value;
        let email = document.getElementById('email').value;

        const myJSON = {
            "name": name, "email": email
        }
        //=====================Needs Action ===================================================
        request("insert_member", myJSON);

        document.getElementById("addSuccessText").classList.remove("hide");

        // close the modal
        closeModal();
    }


}

function editMember(editButton, id) {

    //if checkSave is false, then the user wants to edit  
    // pop up the editing modal
    document.getElementById("addMemberButton").click();

    // get all the data of the current member
    current_card_edit = editButton.parentNode.parentNode.parentNode.parentNode;
    all_card = editButton.parentNode.parentNode.parentNode.parentNode.parentNode;

    var elemArray = [];
    var labelArray = ["Member Name", "email"];
    var cardBody = current_card_edit.children[1];

    elemArray.push(current_card_edit.children[0].children[0].innerHTML); // Membername
    elemArray.push(cardBody.children[3].innerHTML); // email

    // pre fill the values in the edit form
    var myModal = document.querySelector("#exampleModal");

    myModal.querySelector('#memberName').value = elemArray[0];
    myModal.querySelector('#email').value = elemArray[1];

    //change the add button to save and change the onclick
    document.getElementById("modalAddButton").textContent = "Save";
    document.getElementById("modalAddButton").onclick = function () { saveMember(this, id) };

}
//============================================================================
function saveMember(saveButton, id) {
    // check if the user entered data is valid

    result = inputValidation("#exampleModal");

    if (!result) {
        var myModal = document.querySelector("#exampleModal");

        const myJSON = {
            "name": myModal.querySelector('#taskName').value,
            "email": myModal.querySelector('#taskDesc').value
        }
        //=================== Needs Action=========
        request("edit_member", myJSON);


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


    var mName = myModal.querySelector('#memberName').value;
    var mEmail = myModal.querySelector('#email').value;

    var checkEmpty = false;
    // if field is empty
    if (mName == '') {


        myModal.querySelector('#err-tname').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    }

    if (mEmail == '') {

        myModal.querySelector('#err-tdesc').innerHTML = '<p class="text-danger mb-0">This field is required</p>';
        checkEmpty = true;

    }

    return checkEmpty;
}

//=========================================================================================================
function component(data) {
    // reset the page before adding in members to prevent duplicates
    document.getElementById("memberList").innerHTML="";
    var header = document.createElement("h4");
    header.classList.add("mb-4" ,"py-3" ,"rounded-pill" ,"bg-light");
    header.innerHTML="Members";
    document.getElementById("memberList").append(header);

    // update the list of members in local storage
    let memberList = [];

    // create variable for the absolute total time logged
    let totalTime=0;


    data.forEach(member => {
        memberList.push(member.member_name);

        var card = document.createElement('div');
        card.className = "card"; //Apply card class

        // Create card header
        var cardHeader = document.createElement('div');
        var cardHeaderText = document.createElement('h5');
        cardHeaderText.innerHTML = member.member_name;
        cardHeader.classList.add("card-header", "d-flex", "flex-row", "justify-content-center");
        // cardHeaderText.classList.add("col-6");

        cardHeader.append(cardHeaderText);

        // // Append span badge 
        // var badgeCont = document.createElement('label');
        // var badge = document.createElement('span');
        // badge.innerHTML = task.email;
        // badgeCont.classList.add("badge", "bg-secondary", "mx-3", "rounded-circle");
        // badge.classList.add("align-middle", "p-1");

        // if (task.email) {
        //     badgeCont.append(badge);
        // }
        // cardHeader.append(badgeCont);
        // card.append(cardHeader);

        // create card body
        var cardBody = document.createElement('div');
        cardBody.className = "card-body";

        // // append span tag badge
        // var tag = document.createElement('span');
        // tag.classList.add('badge', 'card-text', 'bg-secondary');
        // tag.innerHTML = task.Tag;
        // cardBody.append(tag);

        var member_name = document.createElement('p');
        member_name.classList.add("card-text", "my-1");
        member_name.innerHTML = member.name;
        cardBody.append(member_name);

        //append assigned to 
        var memberEmail = document.createElement('p');
        memberEmail.classList.add("card-text", "my-1");
        memberEmail.innerHTML = member.email;
        cardBody.append(memberEmail);

        //append task date
        // var date = document.createElement('p');
        // date.classList.add("card-text", "my-1");
        // date.innerHTML = task.Deadline;
        // cardBody.append(date);

        // // //append hidden inputs
        // var desc = document.createElement('p');
        // desc.className = "hiddenInput";
        // desc.innerHTML = task.Description;
        // cardBody.append(desc);

        // var status = document.createElement('p');
        // status.className = "hiddenInput";
        // status.innerHTML = task.Status;
        // cardBody.append(status);

        // var priority = document.createElement('p');
        // priority.className = "hiddenInput";
        // priority.innerHTML = task.Priority;
        // cardBody.append(priority);

        // var origin = document.createElement('p');
        // origin.className = "hiddenInput";
        // origin.innerHTML = task.Task_Origin;
        // cardBody.append(origin);

        // member buttons list
        var memberButtonContainer = document.createElement('div');
        memberButtonContainer.className = "container-fluid";
        memberButtonContainer.id = "memberButtonContainer";

        //buttons box div
        var buttonsRow = document.createElement('div');
        buttonsRow.classList.add("row", "justify-content-center");

        //view button
        var viewButton = document.createElement("button");
        viewButton.classList.add("col-2", "btn");
        viewButton.onclick = function () { viewMember(this); };

        //view icon
        var viewIcon = document.createElement('img');
        viewIcon.src = "assets/magnifying-glass-solid.svg";
        viewButton.append(viewIcon);

        buttonsRow.append(viewButton);

        // edit button
        var editButton = document.createElement("button");
        editButton.classList.add("col-2", "btn", "mx-3");
        editButton.onclick = function () { editMember(this, member.id); };

        //edit icon
        var editIcon = document.createElement('img');
        editIcon.src = "assets/pencil-solid.svg";
        editButton.append(editIcon);

        buttonsRow.append(editButton);

        //delete button
        var deleteButton = document.createElement("button");
        deleteButton.classList.add("col-2", "btn");
        deleteButton.onclick = function () { deleteMember(this, member.id); };

        //delete icon
        var deleteIcon = document.createElement('img');
        deleteIcon.src = "assets/trash-solid.svg";
        deleteButton.append(deleteIcon);

        buttonsRow.append(deleteButton);

        memberButtonContainer.append(buttonsRow);


        cardBody.append(memberButtonContainer);
        card.style.backgroundColor = "pink";
        
        // // Color coding the card based on priority level
        // switch (document.getElementById('priorityDropdown').value) {
        //     case "Critical":
        //         card.style.backgroundColor = "pink";
        //         break;
        //     case "High":
        //         card.style.backgroundColor = "#E6B566";
        //         break;
        //     case "Medium":
        //         card.style.backgroundColor = "yellow";
        //         break;
        //     case "Low":
        //         card.style.backgroundColor = "lightgreen";
        //         break;
        // }

        card.append(cardBody);

        // var status = document.getElementById('statusDropdown').value;
        // var memberContainer; ``

        // switch (status) {
        //     case "Pending":
        //         taskContainer = document.getElementById("taskPending");
        //         break;
        //     case "In Progress":
        //         taskContainer = document.getElementById("taskProgress");
        //         break;
        //     case "Peer Review":
        //         taskContainer = document.getElementById("taskReview");
        //         break;
        //     case "Testing":
        //         taskContainer = document.getElementById("taskTesting");
        //         break;
        //     case "Completed":
        //         taskContainer = document.getElementById("taskCompleted");
        //         break;
        // }

        var memberContainer = document.getElementById("memberList");
        memberContainer.append(card);

        let timeList = JSON.parse(localStorage.getItem(member.member_name));
        if(!(timeList==null)){
            for(var i=0; i<timeList.length; i++){
                let hour=timeList[i].split(";")[1];
                totalTime+=hour;
            }
        }
    });

    // add the list of members to local storage
    localStorage.setItem("memberList", JSON.stringify(memberList));

    // this will update the average time and total time spent by all the members                                                                            
    if(memberList.length>0){
        var averageTime = totalTime/(memberList.length);
        updateTimeLogTable(totalTime, averageTime);
    }
}

function graphDateValidation(){
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

    return check;
}

function outputGraph(){
    let check = graphDateValidation()

    let memberList = JSON.parse(localStorage.getItem("memberList"))
    
}


var wow = document.createElement("script")

                    
function graphOpen(){
    var xValues = [100,200,300,400,500,600,700,800,900,1000];
    
    var output = {
    type: "line",
    data: {
        labels: xValues,
        datasets: [{ 
        data: [860,1140,1060,1060,1070,1110,1330,2210,7830,2478],
        borderColor: "red",
        fill: false
        }, { 
        data: [1600,1700,1700,1900,2000,2700,4000,5000,6000,7000],
        borderColor: "green",
        fill: false
        }, { 
        data: [300,700,2000,5000,6000,4000,2000,1000,200,100],
        borderColor: "blue",
        fill: false
        }]
    },
    options: {
        legend: {display: false}
    }
    };

    console.log(output)

    document.getElementById("graphScript").value=output;
}

graphOpen()
