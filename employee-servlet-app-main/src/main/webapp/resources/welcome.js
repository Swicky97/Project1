const reimbBtn = document.getElementById("reimb-btn");
const viewReimbBtn = document.getElementById("view-reimb-btn");
const viewReimbContainer = document.getElementById("view-reimb-container");
const updateInfoBtn = document.getElementById("update-info-btn");
const updateContainer = document.getElementById("update-container");
const viewResolvedReimbBtn = document.getElementById("view-resolved-reimb-btn");
const viewResolvedReimbContainer = document.getElementById("view-resolved-reimb-container");

reimbBtn.addEventListener("click", async function(e) {
    e.preventDefault();
    const amount = document.getElementById("amount").value;
    const description = document.getElementById("description").value;
    let result = await addReimbursment({amount, description});
    console.log(result);
});

viewReimbBtn.addEventListener("click", async () => {
    viewReimbContainer.innerHTML = `
    	<section id="open">
				<h2>Unresolved Requests</h2>
				<table>
					<thead>
						<tr>
							<th>Amount</th>
							<th>Description</th>
							<th>Submitted</th>
						</tr>
					</thead>
					<tbody id="open-body">
						
					</tbody>
				</table>
			</section>
    `;
    getMyReimbursements();
});

viewResolvedReimbBtn.addEventListener("click", async () => {
    viewResolvedReimbContainer.innerHTML = `
    	<section id="open">
				<h2>Resolved Requests</h2>
				<table>
					<thead>
						<tr>
							<th>Amount</th>
							<th>Description</th>
							<th>Submitted</th>
						</tr>
					</thead>
					<tbody id="resolved-body">
						
					</tbody>
				</table>
			</section>
    `;
    getMyResolvedReimbursements();
});

updateInfoBtn.addEventListener("click", async () => {
	let data = await getMe();
	console.log(data);
	updateContainer.innerHTML = `
		<form onsubmit="getUpdate(event)">
		
		<p>First Name: ${data.firstName}&emsp; Last Name: ${data.lastName}&emsp; Username: ${data.username}</p>
		
		<h4>Update profile information below</h4>
		
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

function getMyReimbursements() {
	const oTBody = document.getElementById("open-body");
    fetch("./reimbursement/mine")
        .then(res => res.json())
        .then((reimbursements) => {
            console.log(reimbursements);
            oTBody.innerHTML = "";
            for(let r of reimbursements) {
				if(r.reimbResolved == null){
                console.log(r);
                oTBody.innerHTML += `
                    <tr>
                        <td>${r.reimbAmount}</td>
                        <td>${r.reimbDescription}</td>
                        <td>${new Date(r.reimbSubmitted).toDateString()}</td> 
                    </tr>
                `;
                }
            }
            
        })
        .catch(console.error);
}

function getMyResolvedReimbursements() {
	const rTBody = document.getElementById("resolved-body");
    fetch("./reimbursement/mine/resolved")
        .then(res => res.json())
        .then((reimbursements) => {
            console.log(reimbursements);
            rTBody.innerHTML = "";
            for(let r of reimbursements) {
				if(r.reimbResolved != null){
                console.log(r);
                rTBody.innerHTML += `
                    <tr>
                        <td>${r.reimbAmount}</td>
                        <td>${r.reimbDescription}</td>
                        <td>${new Date(r.reimbSubmitted).toDateString()}</td>
                    </tr>
                `;
                }
            }
            
        })
        .catch(console.error);
}


