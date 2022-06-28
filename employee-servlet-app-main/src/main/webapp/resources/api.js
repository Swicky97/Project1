/**
 * Retrieve current user's user information
 * @returns {Object} containing current user information
 */
async function getMe() {
    return await fetch("./me")
        .then(res => res.json())
        .catch(e => console.error(e));
}

async function getMyReimbursments() {
    return await fetch("./reimbursments/mine")
        .then(res => res.json())
        .catch(e => console.error(e));
}
