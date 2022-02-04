import subprocess
import os
import argparse

def is_filename_in_commit(hash, filename):
    out = subprocess.check_output(['git', 'show', '--pretty=format:', '--name-only', hash]).splitlines()
    outAsFilenames = [os.path.basename(x.decode('UTF-8')) for x in out ]

    for outFilename in outAsFilenames:
        if outFilename == filename:
            return True

    return False

parser = argparse.ArgumentParser(description='Script to find lost commits containing a given filename')
parser.add_argument('-f','--filename', help='The filename to search for in the lost commits', required=True)
args = vars(parser.parse_args())

filename = args['filename']

out = subprocess.check_output(['git', 'fsck', '--lost-found']).splitlines()
outAsColumns = [x.decode('UTF-8').split() for x in out ] # col 1 is the type (commit or blob) and col 2 is the hash
commitsOnlyAsColumns = [x for x in outAsColumns if x[1] == 'commit']

for commit in commitsOnlyAsColumns:
    if is_filename_in_commit(commit[2], filename) is True:
        print('Found file in commit: ' + commit[2])
