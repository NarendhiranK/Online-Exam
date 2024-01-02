import React, { useEffect } from "react";
import { useState } from "react";
import {  Outlet, useNavigate } from "react-router-dom";

const GetExams = () => {
  const [data, setData] = useState("");
  const navigate = useNavigate();
  


  const ExamDetails = (examId) => {
    console.log("examId", examId);
    navigate(`/admin/editExam?examId=${examId}`);
  };

  function getExams() {
    fetch("https://localhost:8443/onlineexam/control/examMasterListEvent")
      .then((response) => {
        return response.json();
      })
      .then((data) => {
        setData(data.ExamData);
        const res = data.ExamData;
        const result = JSON.stringify(res);
        console.log("res....", result);
      });
  }

  useEffect(() => {
    getExams();
  }, []);

  return (
    <div className="container  my-5">
      <div className="col-10 offset-1">
        <table className="table table-striped ">
          <thead>
            <tr>
              <th>ExamName</th>
              <th></th>
              <th>Edit</th>
              <th>Details</th>
              <th>Delete</th>
            </tr>
          </thead>

          <tbody>
            {data ? (
              data.map((obj, value) => (
                <tr key={obj.examId}>
                  <td>{obj.examName}</td>
                  <td>{obj.description}</td>

                  <td>
                    <button
                      className="btn btn-primary"
                      onClick={() => ExamDetails(obj.examId)}
                    >
                      Edit
                    </button>
                  </td>
                  <td>
                    <button
                      className="btn btn-primary"
                      onClick={() => navigate(`examdetails/${obj.examId}`)}
                    >
                      Details
                    </button>
                  </td>
                  <td>
                    <button
                      className="btn btn-danger"
                      // onClick={() => deleteRows(obj.examId)}
                    >
                      Delete
                    </button>
                  </td>
                </tr>
              ))
            ) : (
              <p>No records to show</p>
            )}
          </tbody>
        </table>
      </div>

      <div className="col-4">
        <Outlet />
      </div>
    </div>
  );
};
export default GetExams;
