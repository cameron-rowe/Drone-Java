from __future__ import print_function
import random, os, sys
from subprocess import call
import multiprocessing as mp

class HillClimber(object):
    def __init__(self, numBits = 48):
        self.bitCount = numBits
        self.bits = [0 for x in range(numBits)]
        self.rand = random.Random()

    def init(self, seed = 0):

        if seed == 0:
            self.rand.seed()

        else:
            self.rand.seed(seed)

        self.seed = seed

        self.bits = [self.rand.randint(0,1) for x in range(self.bitCount)]


    def run(self, maxIterations = 1000, resultsFile = 'results.txt'):
        results = open(resultsFile, 'w')
        iterations = 0
        s_0 = list(self.bits)
        f_0 = self.evaluate(s_0)

        while iterations < maxIterations:
            s_1 = self.modify(s_0)
            f_1 = self.evaluate(s_1)

            #print('%d  new fitness: %f   current best: %f' % (iterations, f_1, f_0))

            if f_1 > f_0:
                s_0 = list(s_1)
                f_0 = f_1

            results.write('%f\n' % (f_0,))

            iterations += 1

        self.bits = list(s_0)

        results.close()
        print('Hill Climber Finished')
        print('Number of iterations:', iterations)
        print('resulting fitness:', f_0)
        print('resulting bits:', self.bits)

    def modify(self, bits):
        ret = list(bits)
        index = self.rand.randint(0,self.bitCount-1)
        ret[index] = 0 if ret[index] == 1 else 1


        return ret

    def evaluate(self, bits):
        DEVNULL = open(os.devnull, 'wb')
        bit_string = ''.join([str(bit) for bit in bits])
        call(['sh', 'run.sh', str(self.seed), bit_string], stdout=DEVNULL, stderr=DEVNULL)
        with open('fitness_%d.txt' % (self.seed)) as fin:
            fitness = float(fin.read())

        DEVNULL.close()
        return fitness

    def test_evaluate(self, bits):
        count = 0
        for x in bits:
            if x == 1:
                count += 1

        return float(count) / float(self.bitCount)

def runTests(seeds, run_count):
    hc = HillClimber(39)

    for seed in seeds:
        hc.init(seed)
        hc.run(run_count, 'results_%d.csv' % (seed,))


if __name__ == '__main__':
    if len(sys.argv) > 1:
        seed_count = int(sys.argv[1])

    else:
        seed_count = 12

    if len(sys.argv) > 2:
        run_count = int(sys.argv[2])

    else:
        run_count = 100

    num_processes = max(mp.cpu_count()-1, 1)

    print('Running Hill Climber with %d seeds, %d iterations, using %d processes' % (seed_count, run_count, num_processes))

    ends = [(seed_count / num_processes)+1]

    for i in range(num_processes-1):
        ends.append((seed_count / num_processes) + ends[-1])

    ends[-1] = seed_count + 1

    seeds = []
    prev = 1
    for end in ends:
        seeds.append(list(range(prev,end)))
        prev = end

    print('seeds per process:',seeds)

    process_list = [mp.Process(target=runTests, args=(seed,run_count)) for seed in seeds]

    for p in process_list:
        p.start()

    for p in process_list:
        p.join()

    # end_1 = (seed_count / 3) + 1
    # end_2 = (seed_count / 3) + end_1
    # end_3 = seed_count + 1

    # seeds_1 = list(range(1, end_1))
    # seeds_2 = list(range(end_1, end_2))
    # seeds_3 = list(range(end_2, end_3))

    # p1 = mp.Process(target=runTests, args=(seeds_1, run_count))
    # p2 = mp.Process(target=runTests, args=(seeds_2, run_count))
    # p3 = mp.Process(target=runTests, args=(seeds_3, run_count))

    # p1.start()
    # p2.start()
    # p3.start()

    # p1.join()
    # p2.join()
    # p3.join()

    data = []
    # cleanup csv
    with open('results_all.csv', 'w') as fout:
        for seed in range(1,seed_count+1):
            column = []
            with open('results_%d.csv' % (seed,)) as fin:
                for line in fin:
                    column.append(line.rstrip('\n'))

            data.append(column)

        for i in range(len(data[0])):
            for column in data:
                fout.write(column[i] + ',')

            fout.write('\n')

    print('Run Finished')