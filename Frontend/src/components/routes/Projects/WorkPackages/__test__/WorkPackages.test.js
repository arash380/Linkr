import React from 'react';
import ReactDOM from 'react-dom';
//import WorkPackages from './../../WorkPackages'
import useAxiosGet from "../../../../../hooks/useAxiosGet";
import {setDefaultHeader} from "../../../../../services/api/AxiosInstance";
import { renderHook, act } from '@testing-library/react-hooks';
import {render, cleanup} from '@testing-library/react'
// import { useUser } from "../../../../contexts/userContext";
import '@testing-library/jest-dom'

afterEach(cleanup);

// it("renders WorkPackages without crashing", ()=> {
//     const div = document.createElement("div");
//     ReactDOM.render(<WorkPackages></WorkPackages>, div)
// })


 it("fetches data", async () => {
    setDefaultHeader(
      "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOjAsIm5hbWUiOiJhZG1pbiIsInJvbGUiOiJBRE1JTiIsImV4cCI6MTg2NTI3MDY5MzUyM30=.tRD6FuS8ax8GeHnI7y05seSqLT6tQY1Xb6x3f8je9CQ"
    );
    const { result, waitForValueToChange } = renderHook(() =>
      useAxiosGet("/work-package")
    );

  
    await waitForValueToChange(() => {
      return result.current.isLoading;
    });

    
  
    console.log(result.current.response);
  });
  