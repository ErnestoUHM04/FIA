import chardet

with open('Clusters_spi_global_rankings.csv', 'rb') as f:
    result = chardet.detect(f.read())
    print(result)