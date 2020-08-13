import numpy as np
import csv
import rank_metrics as rm
import glob, os

methods=10987


results=[]
with  open('D:\Implementations\Experiments\Results\CrudResults\Lucene_SWT.csv','r') as f1, open('D:\Implementations\Experiments\SWT\AnswerMatrix_SWT.csv','r') as f, open('VSM_Lucene_SWT.csv','w', newline='') as w, open('VSM_Lucene_10SWT.csv','w', newline='') as w1:
    writer = csv.writer(w)
    writer1 = csv.writer(w1)
    r1, r = csv.reader(f1), csv.reader(f)
    column= np.array([], dtype=int)
    while True:
        try:

            list1=next(r1)
            filter_object = filter(lambda x: x != "", list1)
            list1 = list(filter_object)
            ids=np.array(np.arange(1,methods+1,1,dtype = 'int32'))
            retrieveIds=np.array(list1)
            retIds=np.array(retrieveIds[::2],dtype = 'int32')
            cutter=retIds.size
            retScores=np.array(retrieveIds[1::2],dtype = 'float')

            dif=np.setdiff1d(ids, retIds)
            a=np.concatenate((retIds, dif))
            indexes = np.unique(a, return_index=True)[1]
            a=[a[index] for index in sorted(indexes)]
            a=np.array(a,int)

            score=np.pad(retScores, (0, methods-retScores.size), 'constant')

            a=a[(-score).argsort()]
            score=score[(-score).argsort()]

            zeroIds=a[np.argwhere(score<=0.0)]
            a=a[np.isin(a,zeroIds, invert=True)]

            # comment when not required to perform analysis on top 10%
            #a=a[0:np.int(cutter/10)]
            #score=score[0:np.int(cutter/10)]



            list1=next(r)
            filter_object = filter(lambda x: x != "", list1)
            list1 = list(filter_object)
            list1=np.unique(np.array(list1, dtype=int))
            binary=np.isin(a, list1).astype(int)
            den= np.argwhere(binary==1)
            if np.array(den).size<1:
                mrr=0
            else:
                mrr=(1/(den[0]+1))
            map=rm.average_precision(binary)
            top1=0
            top5=0
            top10=0
            top20=0
            top100=0
            if 1 in binary[:100]:
                top100=1
            if 1 in binary[:20]:
                top20=1
            if 1 in binary[:10]:
                top10=1
            if 1 in binary[:5]:
                top5=1
            if 1 in binary[:1]:
                top1=1

            results.append([mrr,map,top1,top5,top10,top20,top100])


        except StopIteration:
            break
print(np.mean(np.array(results)[:,0:2], axis=0))
print(np.sum(np.array(results, int)[:,2:7], axis=0))
#print(res)
np.savetxt("./Results/SWT_IR_Results.csv", results, fmt='%s')
            #writer1.writerow(list(a))
            #writer1.writerow(list(score))

