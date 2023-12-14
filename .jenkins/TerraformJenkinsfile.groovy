pipeline {
    agent any

    options {
        ansiColor('xterm')
    }

    parameters {
        booleanParam(name: 'TfInit', defaultValue: true, description: 'Terraform Init')
        booleanParam(name: 'TfPlan', defaultValue: true, description: 'Terraform Plan')
        booleanParam(name: 'TfApply', defaultValue: false, description: 'Terraform Apply')
        booleanParam(name: 'TfPlanDestroy', defaultValue: false, description: 'Terraform Plan Destroy')
        booleanParam(name: 'TfDestroy', defaultValue: false, description: 'Terraform Destroy')
        string(name: 'WorkingDirectory', defaultValue: "(Get-Location).Path", description: 'The working directory')
        string(name: 'TerraformVersion', defaultValue: 'latest', description: 'Terraform version')
        booleanParam(name: 'DebugMode', defaultValue: true, description: 'Enable DebugMode')
        // Add more parameters as needed
    }

    environment {
        TFENV_AUTO_INSTALL = 'false'
        ARM_CLIENT_ID = credentials('SpokeClientId')
        ARM_CLIENT_SECRET = credentials('SpokeClientSecret')
        ARM_TENANT_ID = credentials('SpokeTenantId')
        ARM_SUBSCRIPTION_ID = credentials('SpokeSubId')
        BACKEND_STORAGE_SUBSCRIPTION_ID = credentials('SpokeSubId')
        BACKEND_STORAGE_RESOURCE_GROUP_NAME = credentials('SpokeSaRgName')
        BACKEND_STORAGE_ACCOUNT_NAME = credentials('SpokeSaName')
        BACKEND_STORAGE_ACCOUNT_BLOB_CONTAINER_NAME = credentials('SpokeSaBlobContainerName')
    }

    stages {
        stage('Run Script') {
            steps {
                script {
                        pwsh """
                            pwsh -File Run-Terraform.ps1 `
                            -WorkingDirectory $params.WorkingDirectory `
                            -RunTerraformInit $params.TfInit `
                            -RunTerraformPlan $params.TfPlan `
                            -RunTerraformPlanDestroy $params.TfPlanDestroy `
                            -RunTerraformApply $params.TfApply `
                            -RunTerraformDestroy $params.TfDestroy `
                            -DebugMode $params.DebugMode `
                            -DeletePlanFiles $params.DeletePlanFiles `
                            -TerraformVersion $params.TerraformVersion `
                            -BackendStorageSubscriptionId $env:BACKEND_STORAGE_SUBSCRIPTION_ID `
                            -BackendStorageResourceGroupName $env:BACKEND_STORAGE_RESOURCE_GROUP_NAME `
                            -BackendStorageAccountName $env:BACKEND_STORAGE_ACCOUNT_NAME `
                            -BackendStorageAccountBlobContainerName $env:BACKEND_STORAGE_ACCOUNT_BLOB_CONTAINER_NAME `
                            -TerraformStateName $params.TerraformStateName
                        """
                }
            }
        }
    }

    post {
        always {
            deleteDir()
        }
    }
}
