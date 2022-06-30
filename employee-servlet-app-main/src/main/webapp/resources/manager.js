const eTBody = document.getElementById("employees-body");
const rTBody = document.getElementById("resolved-body");
const uTBody = document.getElementById("unresolved-body");
const employeesBtn = document.getElementById("view-employees");
const resolvedBtn = document.getElementById("view-resolved");
const unresolvedBtn = document.getElementById("view-unresolved");
const employeeSection = document.getElementById("employees");
const resolvedSection = document.getElementById("resolved");
const unresolvedSection = document.getElementById("unresolved");

function getOutstandingReimbursements() {
    fetch("./reimbursement/unresolved")
        .then(res => res.json())
        .then((reimbursements) => {
            console.log(reimbursements);
            uTBody.innerHTML = "";
            for(let r of reimbursements) {
                console.log(r);
                uTBody.innerHTML += `
                    <tr>
                        <td>${r.reimbAmount}</td>
                        <td>${r.reimbDescription}</td>
                        <td>${new Date(r.reimbSubmitted).toDateString()}</td>
                        <td>
                            <button data-id=${r.id} onclick="approve(event)">Approve</button>
                            <button data-id=${r.id} onclick="deny(event)">Deny</button>
                        </td>
                    </tr>
                `;
            }
        })
        .catch(console.error);
}

function getEmployees() {
    fetch("./employees")
        .then(res => res.json())
        .then((employees) => {
            console.log(employees);
            eTBody.innerHTML = "";
            for(let e of employees) {
                eTBody.innerHTML += `
                    <tr data-id=${e.id} onclick="viewReimbursements(event)">
                        <td>${e.id}</td>
                        <td>${e.firstName} ${e.lastName}</td>
                        <td>${e.username}</td>
                        <td>${e.role}</td>
                    </tr>
                `;
            }
        })
        .catch(console.error);
}

function approve(e) {
    e.preventDefault();
    const id = e.target.getAttribute("data-id");
    console.log("approve", id);
    fetch(`./reimbursement/approve`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ id })
    }).then(res => {
        if(res.ok) {
            uTBody.removeChild(e.target.parentElement.parentElement);
        }
    })
        .catch(console.error);
}

function deny(e) {
    e.preventDefault();
    const id = e.target.getAttribute("data-id");
    console.log("deny", id);
    fetch(`./reimbursement/deny`, {
        method: "PUT",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify({ id })
    }).then(res => {
        if(res.ok) {
            uTBody.removeChild(e.target.parentElement.parentElement);
        }
    })
        .catch(console.error);
}

employeesBtn.addEventListener("click", () => {
    resolvedSection.classList.remove("visible");
    unresolvedSection.classList.remove("visible");
    getEmployees();
    employeeSection.classList.add("visible");
});

resolvedBtn.addEventListener("click", () => {
    employeeSection.classList.remove("visible");
    unresolvedSection.classList.remove("visible");
    getEmployees();
    resolvedSection.classList.add("visible");
})

unresolvedBtn.addEventListener("click", () => {
    employeeSection.classList.remove("visible");
    resolvedSection.classList.remove("visible");
    getOutstandingReimbursements();
    unresolvedSection.classList.add("visible");
});
