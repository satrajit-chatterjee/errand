'use strict';

const functions = require('firebase-functions');
const admin = require('firebase-admin');
const nodemailer = require('nodemailer');
// const telegramBot = require('node-telegram-bot-api');

//to make it work you need gmail account
const gmailEmail = functions.config().gmail.login;
const gmailPassword = functions.config().gmail.pass;
// const botToken = functions.config().errand_bot.token;
// const botChatID = functions.config().errand_bot.global_chat_id;
// const errandBot = new telegramBot(botToken, {polling: true});

admin.initializeApp();

//creating function for sending emails
var goMail = function (_data, title) {

    var message = JSON.stringify(_data)

//transporter is a way to send your emails
    const transporter = nodemailer.createTransport({
        service: 'gmail',
        auth: {
            user: gmailEmail,
            pass: gmailPassword
        }
    });

    const email = JSON.parse(message);

    // console.log(Object.keys(JSON.parse(message)));
    if (("order_details" in email && title == "Received New Order") || 
        ("feedback" in email && title == "Received New Feedback")){
            // console.log("Yes");
            var to_send = "Customer Name: " + email.name + "\n\n"
            + "Customer Phone Number: " + email.phno + "\n\n"
            + "Customer Email ID: " + email.email + "\n\n"
            + "Customer Delivery Address: " + email.addr + "\n\n"
            
            if (title == "Received New Order")
            to_send = to_send + "Customer Order Details:\n\n\t" + email.order_details;
            else
            to_send = to_send + "Customer Feedback Details:\n\n\t" + email.feedback;

            console.log(to_send);

            const mailOptions = {
                from: gmailEmail, // sender address
                to: ['satrajitdchatterjee@gmail.com', "sanchitadchakraborty@gmail.com", "leerrandcompany@gmail.com"], // list of receivers
                subject: title, // Subject line
                text: to_send, // plain text body
            };

            //this is callback function to return status to firebase console
            const getDeliveryStatus = function (error, info) {
                if (error) {
                    return console.log(error);
                }
                console.log('Message sent: %s', info.messageId);
                // Message sent: <b658f8ca-6296-ccf4-8306-87d57a0b4321@example.com>
            };

            // //call of this function send an email, and return status
            transporter.sendMail(mailOptions, getDeliveryStatus);

            // post telegram bot message to errand_bot
            // errandBot.sendMessage(botChatID, to_send);
        }
    
};


exports.onDataAdded = functions.firestore
    .document('Users/{email}/{active_orders}/{dateTime}')
    .onWrite((snap, context) => {
    const createdData = snap.after.data();
    // console.log(createdData);
    goMail(createdData, "Received New Order");
    return 0;
    // return admin.messaging().sendToDevice(null, null);
    });

exports.onFeedbackAdded = functions.firestore
    .document('Users/{email}/{feedback}/{dateTime}')
    .onWrite((snap, context) => {
    const createdFeedback = snap.after.data();
    // console.log(createdFeedback);
    goMail(createdFeedback, "Received New Feedback");
    return 0;
    // return admin.messaging().sendToDevice(null, null);
    });

