/**
 * Persist a new reimbursment to the database
 * @param {Object} reimbursment with params "amount" and "description"
 * @returns {Object} the new reimbursment
 */
async function addReimbursment(reimbursment) {
    return await fetch("./reimbursement", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(reimbursment)
    })
        .then(res => res.json())
        .catch(e => console.error(e));
}

/**
 * Retrieve current user's user information
 * @returns {Object}
 */
async function getMe() {
    return await fetch("./me")
        .then(res => res.json())
        .catch(e => console.error(e));
}

/**
 * Retrieve list of current user's reimbursments
 * @returns {Object[]}
 */
async function getMyReimbursments() {
    return await fetch("./reimbursement/mine")
        .then(res => res.json())
        .catch(e => console.error(e));
}

/**
 * For retrieving the entire list of reimbursments
 * @returns {Object[]}
 */
async function getReimbursments() {
    return await fetch("./reimbursement")
        .then(res => res.json())
        .catch(e => console.error(e));
}
