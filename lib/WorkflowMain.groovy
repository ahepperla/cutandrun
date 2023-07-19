//
// This file holds several functions specific to the main.nf workflow in the nf-core/cutandrun pipeline
//

import nextflow.Nextflow

//
// This file holds several functions specific to the main.nf workflow in the {{ name }} pipeline
//
class WorkflowMain {

    //
    // Citation string for pipeline
    //
    public static String citation(workflow) {
        return "If you use ${workflow.manifest.name} for your analysis please cite:\n\n" +
            "  https://doi.org/10.5281/zenodo.5653535\n\n" +
            "* The nf-core framework\n" +
            "  https://doi.org/10.1038/s41587-020-0439-x\n\n" +
            "* Software dependencies\n" +
            "  https://github.com/${workflow.manifest.name}/blob/master/CITATIONS.md"
    }

    //
    // Initialise the workflow
    //
    public static void initialise(workflow, params, log) {
        // Print workflow version and exit on --version
        if (params.version) {
            String workflow_version = NfcoreTemplate.version(workflow)
            log.info "${workflow.manifest.name} ${workflow_version}"
            System.exit(0)
        }

        // Check that a -profile or Nextflow config has been provided to run the pipeline
        NfcoreTemplate.checkConfigProvided(workflow, log)

        // Check that conda channels are set-up correctly
        if (workflow.profile.tokenize(',').intersect(['conda', 'mamba']).size() >= 1) {
            Utils.checkCondaChannels(log)
        }

        // Check AWS batch settings
        NfcoreTemplate.awsBatch(workflow, params)

        // Check input has been provided
        if (!params.input) {
            Nextflow.error("Please provide an input samplesheet to the pipeline e.g. '--input samplesheet.csv'")
        }
    }

    //
    // Get attribute from genome config file e.g. fasta
    //
    public static Object getGenomeAttribute(params, attribute) {
        if (params.genomes && params.genome && params.genomes.containsKey(params.genome)) {
            if (params.genomes[ params.genome ].containsKey(attribute)) {
                return params.genomes[ params.genome ][ attribute ]
            }
        }
        return null
    }

    //
    // Get attribute from genome config file e.g. fasta
    //
    public static String getGenomeAttributeSpikeIn(params, attribute) {
        def val = ''
        if (params.genomes && params.spikein_genome && params.genomes.containsKey(params.spikein_genome)) {
            if (params.genomes[ params.spikein_genome ].containsKey(attribute)) {
                val = params.genomes[ params.spikein_genome ][ attribute ]
            }
        }
        return val
    }
}
