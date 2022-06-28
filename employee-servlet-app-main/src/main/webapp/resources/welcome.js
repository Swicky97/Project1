const testBtn = document.getElementById("me-btn");

testBtn.addEventListener("click", async () => {
    let data = await getMe();

    console.log(data);
})
