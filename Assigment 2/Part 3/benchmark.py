import csv
from functools import wraps
import time
import datetime
import math 

"""
:param warmups :  The number of warm-up invokations to fun 
:param iter: The number of times fun must be invoked
:param verbose: hether the execution should be verbose  or not
:param csv_file: A CSV file name where the benchmark information must be written
"""
def benchmark(warmups = 0, iter = 1, verbose = False, csv_file = None):
    def _benchmark(func):
        @wraps(func)
        def wrapper(*args, **kwargs):
            warmUpTimes = []
            iterationTimes = []
           
            # Warm-up
            if warmups > 0 :
                #print("Starting {0} warm-up".format(warmups)) # for debug
                for warmup in range(warmups):
                    start = time.time()
                    #print("Start {}".format(start)) # for debug
                    func(*args, **kwargs)
                    end = time.time()
                    #print("End {}".format(end)) # for debug
                    elapsed = end - start
                    warmUpTimes.append(elapsed)
                    #print("{0} spend {%.2f} seconds in warmup".format(warmup, elapsed)) # for debug  
                #print("Ending {0} Warming up".format(warmups)) # for debug

            # Iteration
            if iter >= 1:
                for x in range(iter):
                    
                    start = time.time()
                    #print("Start {}".format(start)) # for debug
                    func(*args, **kwargs)
                    end = time.time()
                    #print("End {}".format(end)) # for debug
                    elapsed = end - start 
                    iterationTimes.append(elapsed)
                    #print("Passed time {0} seconds".format(datetime.timedelta(seconds =elapsed)))

            # Verbose
            if verbose:
                print("----------------- Warm-Up ------------------1")

                index = 1
                print("|--------------------|")
                print("| # | Warm-up Time   |")
                print("|--------------------|")
                for t in  warmUpTimes: # t = time 
                    print("| {0} | {1} seconds |".format(index,round(t,4)))
                    print("|--------------------|")
                    index += 1
                print("----------------- Itertion ------------------1")
                index = 1
                print("|--------------------|")
                print("| # | Iteration Time |")
                print("|--------------------|")
                for t in  iterationTimes: # t = time 
                    print("| {0} | {1} seconds |".format(index,round(t,4)))
                    print("|--------------------|")
                    index += 1
                    
            # CSV File
            if csv_file != None:
                print(csv_file)
                with open(csv_file, mode='w') as csv_time:
                    fieldnames = ['run_num', 'is_warmup', 'timing']
                    writer = csv.DictWriter(csv_time, fieldnames=fieldnames)
                    writer.writeheader()
                    index = 1
                    for x in warmUpTimes:
                        writer.writerow({'run_num':index, 'is_warmup':True, "timing":x})
                        index +=1

                    for x in iterationTimes:
                        writer.writerow({'run_num':index, 'is_warmup':False, "timing":x})
                        index +=1


                # Printing information end of the benchmark
                if (warmups >= 1):
                    print("\n##### Info  Warm-up  #####")
                    warmMean = sum(warmUpTimes)/len(warmUpTimes)
                    squareSum = 0.0

                    for x in warmUpTimes:
                        squareSum += ((x - warmMean)**2)
                    
                    warmVariance = squareSum / len(warmUpTimes)
                    print("Average Time =  {0} ".format(warmMean))
                    print("Variance Time =  {0} ".format(warmVariance))
                
                if (iter >= 1):                      
                    iterMean = sum(iterationTimes)/len(iterationTimes)
                    squareSum = 0.0

                    for x in iterationTimes:
                        squareSum += ((x - iterMean)**2)
                    iterVariance = squareSum / len(iterationTimes)
                    print("\n\n##### Info Iteration #####")
                    print("Average Time =  {0} ".format(iterMean))
                    print("Variance Time =  {0} ".format(iterVariance))
                    print("\n")

        return wrapper
    return _benchmark


@benchmark(warmups=5,iter=5,verbose=True,csv_file="results.csv")
def fun():
    time.sleep(0.5)

fun()