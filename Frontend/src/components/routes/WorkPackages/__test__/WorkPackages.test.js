import React from 'react';
import ReactDOM from 'react-dom';
import WorkPackages from './../WorkPackages'
import useAxiosGet from "../../../../hooks/useAxiosGet";
import {render, cleanup} from '@testing-library/react'
import { useUser } from "../../../../contexts/userContext";
import '@testing-library/jest-dom'

afterEach(cleanup);

it("renders WorkPackages without crashing", ()=> {
    const div = document.createElement("div");
    ReactDOM.render(<WorkPackages></WorkPackages>, div)
})
