const testBtn = document.getElementById("me-btn");
const meContainer = document.getElementById("me-container");
const reimbBtn = document.getElementById("reimb-btn");
const viewReimbBtn = document.getElementById("view-reimb-btn");
const myReimbContainer = document.getElementById("view-reimb-container");
const updateInfoBtn = document.getElementById("update-info-btn");
const updateContainer = document.getElementById("update-container");

testBtn.addEventListener("click", async () => {
    let data = await getMe();
    console.log(data);
    meContainer.innerHTML = `
        <p>First Name: ${data.firstName} Last Name: ${data.lastName} Username: ${data.username}</p>
    `;
});

reimbBtn.addEventListener("click", async function(e) {
    e.preventDefault();
    const amount = document.getElementById("amount").value;
    const description = document.getElementById("description").value;
    let result = await addReimbursment({amount, description});
    console.log(result);
});

viewReimbBtn.addEventListener("click", async () => {
    let data = await getMyReimbursments();
    console.log(data);
    myReimbContainer.innerHTML = `
    	<p>${data.reimbAmount}</p>
    `;
});

updateInfoBtn.addEventListener("click", async () => {
	let data = await getMe();
	console.log(data);
	updateContainer.innerHTML = `
		<form method="POST" action="update" onsubmit="getUpdate(event)">
		
		<p>First Name: ${data.firstName} Last Name: ${data.lastName} Username: ${data.username}</p>
		
		<label>First Name: </label> 
		<input id="first-name" type="text" name="firstname" placeholder="Enter new first name"> <br /> 
		
		<label>Last Name: </label> 
		<input id="last-name" type="text" name="lastname" placeholder="Enter new last name"> <br /> 

		<label>Username: </label> 
		<input id="username" type="text" name="username" placeholder="Enter new username"> <br /> 
			
		<label>Password: </label>
		<input id="password" type="password" name="password" placeholder="Enter new password">

		<input type="submit" value="Update information">

		</form>
	`;
});

function getUpdate(e) {
	e.preventDefault();
    const firstName = document.getElementById("first-name").value;
    const lastName = document.getElementById("last-name").value;
    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;
	//TODO: Grab other form input values
	console.log(firstName);
	console.log(lastName);
	console.log(username);
	console.log(password);
    fetch(`./update`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ firstName, lastName, username, password })
    }).then(res => res.json())
        .catch(e => console.error(e));
}
