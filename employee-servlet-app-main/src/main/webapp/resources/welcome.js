const testBtn = document.getElementById("me-btn");
const meContainer = document.getElementById("me-container");
const reimbBtn = document.getElementById("reimb-btn");

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
