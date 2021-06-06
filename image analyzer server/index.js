const express = require('express')
const { PythonShell } = require("python-shell");
const app = express()
const fs = require('fs');
const path = require('path');
const request = require('request');
var buffer = require('buffer');
const utf8 = require('utf8');
const multer = require('multer');
var bodyParser = require('body-parser');
//패키지


//이미지 업로드 설정
const upload = multer({
    storage: multer.diskStorage({
        destination: function(req, file, cb) {
            cb(null, 'img/');
        },
        filename: function(req, file, cb) {
            cb(null, file.originalname);
        }
    }),
    dest: 'uploads/',
    limits: { fileSize: 100 * 2560 * 1440 }
});
const port = 3000



//기본 테스트용
app.get('/', (req, res) => {
    res.send('Hello World!')
})

//multer 이미지 수힌하여 실행
app.post('/analysis', upload.single('img'), (req, res) => {
    console.log(req.body);
    console.log(req.file); // 이미지 정보
    console.log(req.file.filename) //이미지 이름

    //파이썬 설정
    var options = {
        mode: 'text',
        pythonPath: '',
        pythonOptions: ['-u'],
        scriptPath: '',
        args: [req.file.filename] //파이썬으로 전송하는 args
    }

    //파이썬 실행 문
    PythonShell.run('Analysis_model.py', options, function(err, results) {
        if (err) throw err;

        console.log('results: %j', results); //결과 출력
        re = results.toString();
        res.send(re); //결과 전송
        //res.json(re);
    })
});


//서버 확인
app.listen(port, () => {
    console.log(`Example app listening at http://localhost:${port}`)
})