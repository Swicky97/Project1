const eTBody = document.getElementById("employees-body");
const erTBody = document.getElementById("employee-requests-body");
const rTBody = document.getElementById("resolved-body");
const uTBody = document.getElementById("unresolved-body");
const employeesBtn = document.getElementById("view-employees");
const resolvedBtn = document.getElementById("view-resolved");
const unresolvedBtn = document.getElementById("view-unresolved");
const employeeSection = document.getElementById("employee-single")
const employeesSection = document.getElementById("employees");
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
                // TODO: Implement veiwReimbursements
                eTBody.innerHTML += `
                    <tr data-id=${e.id} data-username=${e.username} ${e.role == "Employee" ? 'class="clickable" onclick="viewReimbursements(event)"' : ""}>
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

function getResolvedReimbursements() {
    fetch("./reimbursement/resolved")
        .then(res => res.json())
        .then((reimbursements) => {
            rTBody.innerHTML = "";
            console.log(reimbursements);
            for(let r of reimbursements) {
                rTBody.innerHTML += `
                    <tr>
                        <td>${r.amount}</td>
                        <td>${r.description}</td>
                        <td>${new Date(r.submitted).toDateString()}</td>
                        <td>${new Date(r.resolved).toDateString()}</td>
                        <td>${r.resolver}</td>
                        <td>${r.approved ? "Approved" : "Denied"}</td>
                    </tr>
                `;
            }
        })
}

function viewReimbursements(e) {
    resolvedSection.classList.remove("visible");
    unresolvedSection.classList.remove("visible");
    employeesSection.classList.remove("visible");
    employeeSection.classList.add("visible");
    e.preventDefault();
    const authorId = e.target.parentElement.getAttribute("data-id");
    const username = e.target.parentElement.getAttribute("data-username");
    console.log(authorId, username);
    document.getElementById("employee-name").innerText = username;
    fetch(`./reimbursement?authorId=${authorId}`)
        .then(res => res.json())
        .then(reimbursements => {
            console.log(reimbursements);
            erTBody.innerHTML = "";
            for(let r of reimbursements) {
                erTBody.innerHTML += `
                    <tr>
                        <td>${r.reimbAmount}</td>
                        <td>${r.reimbDescription}</td>
                        <td>${new Date(r.reimbSubmitted).toDateString()}</td>
                        <td>${r.reimbResolved != null ? new Date(r.reimbResolved).toDateString() : "Unresolved"}</td>
                        <td>
                        ${ r.reimbResolved != null ? (r.reimbApproved ? "Approved" : "Denied") :
                            `<button data-id=${r.id} onclick="approve(event)">Approve</button>
                            <button data-id=${r.id} onclick="deny(event)">Deny</button>`
                        }
                        </td>
                    </tr>
                `;
            }
        })

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
            e.target.parentElement.innerHTML = "Approved!"
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
            e.target.parentElement.innerHTML = "Denied";
        }
    })
        .catch(console.error);
}

employeesBtn.addEventListener("click", () => {
    employeeSection.classList.remove("visible");
    resolvedSection.classList.remove("visible");
    unresolvedSection.classList.remove("visible");
    getEmployees();
    employeesSection.classList.add("visible");
});

resolvedBtn.addEventListener("click", () => {
    employeeSection.classList.remove("visible");
    employeesSection.classList.remove("visible");
    unresolvedSection.classList.remove("visible");
    getResolvedReimbursements();
    resolvedSection.classList.add("visible");
});

unresolvedBtn.addEventListener("click", () => {
    employeeSection.classList.remove("visible");
    employeesSection.classList.remove("visible");
    resolvedSection.classList.remove("visible");
    getOutstandingReimbursements();
    unresolvedSection.classList.add("visible");
});
