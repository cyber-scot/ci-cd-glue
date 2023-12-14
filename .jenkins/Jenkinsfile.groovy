pipeline {
    agent any

    options {
        ansiColor('xterm')
    }

    parameters {
        booleanParam(name: "TfInit", defaultValue: true, description: 'Terraform Init')
        booleanParam(name: "TfPlan", defaultValue: true, description: 'Terraform Plan')
        booleanParam(name: "TfApply", defaultValue: false, description: 'Terraform Apply')
        booleanParam(name: "TfPlanDestroy", defaultValue: false, description: 'Terraform Plan Destroy')
        booleanParam(name: "TfDestroy", defaultValue: false, description: 'Terraform Destroy')
        string(name: 'WorkingDirectory', defaultValue: '$WORKSPACE', description: 'The working directory')
        string(name: 'TerraformVersion', defaultValue: 'latest', description: 'Terraform version')
        booleanParam(name: "DebugMode", defaultValue: true, description: 'Enable DebugMode')
    }

    environment {
        TFENV_AUTO_INSTALL = 'false'
    }

    stages {
        stage('Run Script') {
            steps {
                script {
                    pwsh """
                        pwsh -File Run-Terraform.ps1 `
                        -WorkingDirectory ${{ inputs.working_directory }} `
                        -RunTerraformInit ${{ inputs.run_terraform_init }} `
                        -RunTerraformPlan ${{ inputs.run_terraform_plan }} `
                        -RunTerraformPlanDestroy ${{ inputs.run_terraform_plan_destroy }} `
                        -RunTerraformApply ${{ inputs.run_terraform_apply }} `
                        -RunTerraformDestroy ${{ inputs.run_terraform_destroy }} `
                        -DebugMode ${{ inputs.enable_debug_mode }} `
                        -DeletePlanFiles ${{ inputs.delete_plan_files }} `
                        -TerraformVersion ${{ inputs.terraform_version }} `
                        -BackendStorageSubscriptionId ${{ secrets.SpokeSubId }} `
                        -BackendStorageResourceGroupName ${{ secrets.SpokeSaRgName }} `
                        -BackendStorageAccountName ${{ secrets.SpokeSaName }} `
                        -BackendStorageAccountBlobContainerName ${{ secrets.SpokeSaBlobContainerName }} `
                        -TerraformStateName ${{ inputs.terraform_state_name }}
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
