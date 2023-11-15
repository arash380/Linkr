import React from 'react';
import ReactDOM from 'react-dom';
import EmployeeForm, { EMP_FORM_MODES as modes } from "./../EmployeeForm";
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {render, cleanup, screen} from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { useUser } from "../../../../contexts/userContext";
import { setDefaultHeader } from "../../../../services/api/AxiosInstance";
import '@testing-library/jest-dom'
import { renderHook, act } from '@testing-library/react-hooks';


const DEFAULT_MODAL_STATE = { show: false, mode: null, emp: null };
let employee
afterEach(cleanup);

beforeAll(async () => {
  setDefaultHeader(
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
  );
  const { result, waitForValueToChange } = renderHook(() =>
    useAxiosGet("/employee")
  );

  await waitForValueToChange(() => {
    return result.current.isLoading;
  });

  employee = result.current.response[1];
});



it("renders EmployeeForm without crashing", ()=> {
    const div = document.createElement("div");
    ReactDOM.render(<EmployeeForm></EmployeeForm>, div)
})


it("correctly changes modes", async ()=> {
    // const {result} = renderHook(useAxiosGet('/employee'));
    // console.log(result);
    global.fetch = jest.fn()

    await act(async () => renderHook(() => useAxiosGet("/employee")))
    expect
    render(<EmployeeForm
     mode={"edit"}
     emp={{ }}
    />)
  //  screen.debug();
})

it("correctly change first name", async ()=> {
  // const {result} = renderHook(useAxiosGet('/employee'));
  // console.log(result);
  global.fetch = jest.fn()

  await act(async () => renderHook(() => useAxiosGet("/employee")))

  render(<EmployeeForm
   mode={"edit"}
   emp={{employee }}
  />)
  userEvent.type(screen.getByRole('textbox', {
    name: /first name/i
  }), "Marie")
  userEvent.click(screen.getByRole('button', {
    name: /save/i
  }))
  const messageTextArea = screen.getByDisplayValue('Marie')
  expect(messageTextArea).toHaveValue("Marie");
  //screen.debug();
  
  userEvent.type(screen.getByRole('textbox', {
    name: /first name/i
  }), "Mary")
  userEvent.click(screen.getByRole('button', {
    name: /save/i
  }))
})

it("correctly change last name", async ()=> {
  // const {result} = renderHook(useAxiosGet('/employee'));
  // console.log(result);
  global.fetch = jest.fn()

  await act(async () => renderHook(() => useAxiosGet("/employee")))

  render(<EmployeeForm
   mode={"edit"}
   emp={{employee }}
  />)
  userEvent.type(screen.getByRole('textbox', {
    name: /last name/i
  }), "Smit")
  userEvent.click(screen.getByRole('button', {
    name: /save/i
  }))
  const messageTextArea = screen.getByDisplayValue('Smit')
  expect(messageTextArea).toHaveValue("Smit");
  //screen.debug();
  
  userEvent.type(screen.getByRole('textbox', {
    name: /last name/i
  }), "Smith")
  userEvent.click(screen.getByRole('button', {
    name: /save/i
  }))
})

it("Checks and unchecks HR and salary employee", async ()=> {
  // const {result} = renderHook(useAxiosGet('/employee'));
  // console.log(result);
  global.fetch = jest.fn()

  await act(async () => renderHook(() => useAxiosGet("/employee")))

  render(<EmployeeForm
   mode={"edit"}
   emp={{employee }}
  />)
  userEvent.click(screen.getByRole('checkbox', {
    name: /hr employee/i
  }))
  //screen.debug()

  expect(screen.getByRole('checkbox', {
    name: /hr employee/i
  })).toBeChecked
  //screen.debug();
  
  userEvent.click(screen.getByRole('checkbox', {
    name: /hr employee/i
  }))

})







