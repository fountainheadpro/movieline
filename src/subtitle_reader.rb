require './split_images.rb'



file_name='pulp_fiction'

def read_subtitles(file_name)
  blocks=[]
  current_block={}
  File.open("#{file_name}.srt").each do |data|
   line=data.strip
   #stupid placeholder for whatever reason the digits matching does not work
   if line.match(/\A\d+\z/) || line=="1"#.length>0 && line.length<=3
     current_block={number: line, lines: []}
   elsif line.match(/^\d\d:\d\d:\d\d,\d\d\d --> \d\d:\d\d:\d\d,\d\d\d$/)
     start, finish=line.gsub(',', '.').split(' --> ')
     length=(finish.split(':').last.to_f-start.split(':').last.to_f)
     length+=60.to_f if length<0
     current_block[:start]=start
     current_block[:length]=length.round(2)
   elsif line.length>0
     current_block[:lines]||=[]
     current_block[:lines]<<line
   else
     current_block[:content]=current_block[:lines].join(' ')
     blocks<<current_block
   end
  end
  blocks
end

blocks=read_subtitles(file_name)

def build_index(blocks)
  index={}

  blocks.each{|block|
    block[:lines].each{|line|
      line.split(" ").each{|piece|
        token=piece.gsub(/[^a-z']/i, '').downcase
        if token.length>0
          index[token]||=Set.new
          index[token]<<block[:number]
        end
      }
    }
  }
  index
end

index=build_index(blocks)

def search(index,phrase)
  phrase.split(" ").map{|word| index[word.downcase]}.reduce(&:&)
end


sel.each_with_index{|block|
  i=block[:number]
  `mkdir ./tmp/#{i}`
  `ffmpeg -ss #{block[:start]} -i #{file_name}.mp4 -t #{block[:length]} -s 480x270  -f image2 ./tmp/#{i}/z%03d.png`
  process_block(i)
}









