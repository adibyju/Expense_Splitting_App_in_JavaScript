const express = require("express");
const router = express.Router();

const {validateToken} = require("./middlewares/loginMiddleware")
const {addEvent,getEvents,getEventDetails,markEventAsCompleted} = require("../services/events")
const {addParicipant, getParticipants, removeParticipant} = require("../services/participants")
const {addItem, getItems, deleteItem, updateItem} = require("../services/items")
const {getId} = require("../services/users")


router.get("/", validateToken, async (req,res) => {
 console.log(req.body)

 response = await getEvents({user_id:req.session.user_id})

 res.json({ eventsList : response})
})

router.post("/mark/:eventID", validateToken, async (req,res) => {
 console.log(req.params.eventID)

 response = await markEventAsCompleted({event_id: req.params.eventID})

 res.json(express.response)
})


router.get("/event/:eventID", validateToken, async (req,res) => {
 console.log(req.params)
 response = await getEventDetails({event_id: Number(req.params.eventID)})
 
 res.json(response)
})

router.get("/participants/:eventID", validateToken, async (req,res) => {
 console.log(req.params)
 response = await getParticipants({event_id: Number(req.params.eventID)})
 res.json({participants : response})
})

router.get("/items/:eventID", validateToken, async (req,res) => {
 console.log(req.params)
 response = await getItems({event_id: Number(req.params.eventID)})
 res.json({items : response})
})

router.post("/create", validateToken, async (req,res) => {
 response = await addEvent({ user_id:req.session.user_id, event: req.body.eventdata })
 res.json(response)
})

router.post("/addparty/:eventID", validateToken, async (req,res) => {
 console.log(req.body)
 var new_user_id
 if(req.body.id)
 {
  new_user_id = req.body.id 
 }
 else if(req.body.mobileno)
 {
  new_user_id=await getId({mobileno: req.body.mobileno})
 }
 else if(req.body.email)
 {
  new_user_id=await getId({email: req.body.email})
 }
 if(new_user_id)
 {
  response = await addParicipant({
   event_id: Number(req.params.eventID),
   user_id: new_user_id
  })
 
  return res.json(response)
 }
 return res.json({success: false, msg : "user does not exist"})
})




module.exports = router