# PowerShell script to set user-level environment variables with predefined values

# Define a hashtable with key-value pairs for environment variables
$predefinedVariableValues = @{
    "WORKING_DIRECTORY" = "example_working_directory"
    "RUN_TERRAFORM_INIT" = "true"
    "RUN_TERRAFORM_PLAN" = "true"
    "RUN_TERRAFORM_PLAN_DESTROY" = "false"
    "RUN_TERRAFORM_APPLY" = "false"
    "RUN_TERRAFORM_DESTROY" = "false"
    "ENABLE_DEBUG_MODE" = "true"
    "DELETE_PLAN_FILES" = "true"
    "TERRAFORM_VERSION" = "latest"
    "BACKEND_STORAGE_SUBSCRIPTION_ID" = "example_subscription_id"
    "BACKEND_STORAGE_USES_AZURE_AD" = "true"
    "BACKEND_STORAGE_RESOURCE_GROUP_NAME" = "example_resource_group_name"
    "BACKEND_STORAGE_ACCOUNT_NAME" = "example_account_name"
    "BACKEND_STORAGE_ACCOUNT_BLOB_CONTAINER_NAME" = "example_blob_container_name"
    "TERRAFORM_STATE_NAME" = "example_state_name"
}

# Set each predefined variable in the user environment
foreach ($varName in $predefinedVariableValues.Keys) {
    $value = $predefinedVariableValues[$varName]

    # Set user environment variable
    [System.Environment]::SetEnvironmentVariable($varName, $value, [System.EnvironmentVariableTarget]::User)
}

Write-Host "User-level environment variables have been set."
