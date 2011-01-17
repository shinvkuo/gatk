package org.broadinstitute.sting.utils.threading;

import org.apache.log4j.Logger;
import org.broadinstitute.sting.utils.GenomeLoc;

import java.util.ArrayList;
import java.util.List;

/**
 * For algorithmic testing purposes only.  Uses synchronization to keep a consistent
 * processing list in shared memory.
 */
public class SharedMemoryGenomeLocProcessingTracker extends GenomeLocProcessingTracker {
    private static Logger logger = Logger.getLogger(SharedMemoryGenomeLocProcessingTracker.class);
    protected List<ProcessingLoc> processingLocs = new ArrayList<ProcessingLoc>();

    public ProcessingLoc claimOwnership(GenomeLoc loc, String myName) {
        // processingLocs is a shared memory synchronized object, and this
        // method is synchronized, so we can just do our processing
        synchronized (processingLocs) {
            ProcessingLoc owner = super.findOwner(loc);

            if ( owner == null ) { // we are unowned
                owner = new ProcessingLoc(loc, myName);
                processingLocs.add(owner);
            }

            return owner;
            //logger.warn(String.format("%s.claimOwnership(%s,%s) => %s", this, loc, myName, owner));
        }
    }

    protected List<ProcessingLoc> getProcessingLocs() {
        synchronized (processingLocs) {
            return processingLocs;
        }
    }
}
