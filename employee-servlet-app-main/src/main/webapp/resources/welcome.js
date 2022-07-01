const testBtn = document.getElementById("me-btn");
const meContainer = document.getElementById("me-container");
const reimbBtn = document.getElementById("reimb-btn");
const viewReimbBtn = document.getElementById("view-reimb-btn");
const myReimbContainer = document.getElementById("view-reimb-container");

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