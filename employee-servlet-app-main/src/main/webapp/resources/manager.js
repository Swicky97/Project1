const tbody = document.querySelector("tbody");

function getOutstandingReimbursements() {
    fetch("./reimbursement/unresolved")
        .then(res => res.json())
        .then((reimbursements) => {
            console.log(reimbursements);
            for(let r of reimbursements) {
                console.log(r);
                tbody.innerHTML += `
                    <tr>
                        <td>${r.reimbAmount}</td>
                        <td>${r.reimbDescription}</td>
                        <td>
                            <button data-id=${r.id} onclick="approve(event)">Approve</button>
                            <button data-id=${r.id} onclick="deny(event)">Deny</button>
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
            tbody.removeChild(e.target.parentElement.parentElement);
        }
    })
}

getOutstandingReimbursements();
