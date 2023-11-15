import React from 'react';
import ReactDOM from 'react-dom';
import ProjectForm from './../ProjectForm'
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {setDefaultHeader} from "../../../../services/api/AxiosInstance";
import { renderHook, act } from '@testing-library/react-hooks';
import userEvent from '@testing-library/user-event'
import {render, cleanup, screen} from '@testing-library/react'
import { useUser } from "../../../../contexts/userContext";
import '@testing-library/jest-dom'

afterEach(cleanup);
let employees
beforeAll(async () => {
    setDefaultHeader(
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
      );
   
})

it("renders ProjectForm without crashing", async ()=> {
    const { result, waitForValueToChange } = renderHook(() =>
    useAxiosGet("/employee")
  );

  


  await waitForValueToChange(() => {
    return result.current.isLoading;
  });


  employees = result.current.response;

    const div = document.createElement("div");
    ReactDOM.render(<ProjectForm
        empList={employees.filter(
            (emp) => emp.employeeID === emp.employeeID 
          )}></ProjectForm>, div)
})

it("correctly fills out form", async ()=> {
  // const {result} = renderHook(useAxiosGet('/employee'));
  // console.log(result);
  global.fetch = jest.fn()

  await act(async () => renderHook(() => useAxiosGet("/project")))


  // screen.getByRole('heading', {
  //   name: /test project/i
  // })
  // userEvent.type(screen.getByRole('textbox', {
  //   name: /project name/i
  // }), "Test porject name")

  // userEvent.type(screen.getByRole('textbox', {
  //   name: /project manager/i
  // }), "Joe Smith")

  // userEvent.type(screen.getByRole('textbox', {
  //   name: /project assistant/i
  // }), "Mary Smith")

  // userEvent.type(screen.getByRole('spinbutton', {
  //   name: /budget/i
  // }), "200")

  // userEvent.type(screen.getByRole('spinbutton', {
  //   name: /project markup/i
  // }), "1")


  // const messageTextArea = screen.getByDisplayValue('Smit')
  // expect(messageTextArea).toHaveValue("Smit");
  // //screen.debug();
  
  // userEvent.type(screen.getByRole('textbox', {
  //   name: /last name/i
  // }), "Smith")
  // userEvent.click(screen.getByRole('button', {
  //   name: /save/i
  // }))
})



it("fetches data", async () => {
    setDefaultHeader(
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
    );
    const { result, waitForValueToChange } = renderHook(() =>
      useAxiosGet("/employee")
    );

  
    await waitForValueToChange(() => {
      return result.current.isLoading;
    });
  
    console.log(result.current.response);
  });
  