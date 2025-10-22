package com.cs203.core.service.impl;

import com.cs203.core.service.CalculationHistoryService;

public class CalculationHistoryImpl implements CalculationHistoryService{
    //save
    /*
     * Return 401 if token missing/invalid
     * First check if its valid user, if role ≠ TRADER return 403
     * Dto to obtain all params needed to save, return 400 if missing fields
     * All correct, save to user entity, get UserId from Dto and call userEntity from repo - linked to correct user ID
     * 
     * 
     */
    //get
    /*
     * Return 401 if token missing/invalid
     * First check if valid user, if role ≠ TRADER return 403
     * Returns the calculation by SavedCalculationId and the user that created it
     * 
     */
}
