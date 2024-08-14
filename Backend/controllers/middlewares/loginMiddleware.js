const validateToken = (req, res, next) => {
    console.log("validation")
    if(!req.session.authorized)
        return res.json(
            {err: "User not logged in! Redirecting to login page...."}
        )

    return next()
}

module.exports = {validateToken}