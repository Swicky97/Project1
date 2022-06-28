const testBtn = document.getElementById("me-btn");
const reimbBtn = document.getElementById("reimb-btn");

testBtn.addEventListener("click", async () => {
    let data = await getMe();
    console.log(data);
});

reimbBtn.addEventListener("click", async function(e) {
    e.preventDefault();
    const amount = document.getElementById("amount").value;
    const description = document.getElementById("description").value;
    let result = await addReimbursment({amount, description});
    console.log(result);
});
